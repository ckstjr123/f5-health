package f5.health.app.service.healthreport;

import f5.health.app.entity.healthreport.HealthReport;
import f5.health.app.entity.healthreport.PromptCompletion;
import f5.health.app.entity.meal.EatenFoodMap;
import f5.health.app.entity.meal.Meal;
import f5.health.app.entity.member.Member;
import f5.health.app.exception.global.DuplicateEntityException;
import f5.health.app.exception.global.NotFoundException;
import f5.health.app.jwt.JwtMember;
import f5.health.app.repository.FoodRepository;
import f5.health.app.repository.HealthReportRepository;
import f5.health.app.repository.MemberRepository;
import f5.health.app.service.healthreport.openai.GptService;
import f5.health.app.service.healthreport.openai.prompt.HealthFeedbackPrompt;
import f5.health.app.service.healthreport.openai.prompt.HealthItemsRecommendPrompt;
import f5.health.app.service.healthreport.vo.MealsNutritionContents;
import f5.health.app.service.healthreport.vo.request.DateRangeQuery;
import f5.health.app.service.healthreport.vo.request.HealthReportRequest;
import f5.health.app.service.healthreport.vo.request.MealsRequest;
import f5.health.app.service.healthreport.vo.request.healthkit.HealthKit;
import f5.health.app.vo.healthreport.response.HealthReportResponse;
import f5.health.app.vo.member.response.HealthLifestyleScore;
import f5.health.app.vo.member.response.HealthLifestyleScoreList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static f5.health.app.exception.healthreport.HealthReportErrorCode.DUPLICATED_REPORT_SUBMIT;
import static f5.health.app.exception.healthreport.HealthReportErrorCode.NOT_FOUND_REPORT;
import static f5.health.app.exception.member.MemberErrorCode.NOT_FOUND_MEMBER;
import static f5.health.app.service.healthreport.openai.prompt.DefaultPromptCompletion.SAVED_MONEY_DEFAULT_COMPLETION;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthReportService {

    public static final int MIN_SAVED_MONEY_REQUIRED = 5000;
    private final MemberRepository memberRepository;
    private final FoodRepository foodRepository;
    private final GptService gptService;
    private final HealthLifeStyleScoreCalculator healthLifeStyleScoreCalculator = new HealthLifeStyleScoreCalculator();
    private final HealthReportRepository reportRepository;

    /** 일자별 조회 */
    @Transactional(readOnly = true)
    public HealthReportResponse findReport(Long memberId, LocalDate endDate) {
        HealthReport report = reportRepository.findByMemberIdAndEndDate(memberId, endDate)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_REPORT));
        Member writer = report.getMember();
        return new HealthReportResponse(report, writer.getRecommendedCalories(), report.getMeals());
    }

    /** 날짜 범위로 점수 조회 */
    public HealthLifestyleScoreList findScores(JwtMember loginMember, DateRangeQuery dateRange) {
        List<HealthLifestyleScore> scores = reportRepository.findScoresByMemberIdAndEndDateBetween(
                loginMember.getId(), dateRange.getStart(), dateRange.getEnd()
        );
        return HealthLifestyleScoreList.from(scores);
    }

    /** 리포트 등록 */
    @Transactional
    public void submit(Long memberId, HealthReportRequest reportRequest) {
        LocalDateTime endDateTime = reportRequest.getEndDateTime();
        this.validateDuplicateReport(memberId, endDateTime.toLocalDate());

        // 절약 금액 로직
        Member writer = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));
        HealthKit healthKit = reportRequest.getHealthKit();
        this.accumulateSavingsAndRecommend(writer, healthKit);

        // 식단
        List<Meal> meals = this.createMeals(reportRequest.getMealsRequest());
        MealsNutritionContents nutritionContents = MealsNutritionContents.from(meals);

        HealthReport report = HealthReport.builder(writer, meals)
                .healthLifeScore(healthLifeStyleScoreCalculator.calculate(HealthSnapshot.of(writer, healthKit, nutritionContents)))
                .waterIntake(healthKit.getWaterIntake())
                .smokeCigarettes(healthKit.getSmokedCigarettes())
                .alcoholDrinks(healthKit.getConsumedAlcoholDrinks())
                .healthFeedback(gptService.call(new HealthFeedbackPrompt(writer, healthKit, nutritionContents)))
                .startDateTime(reportRequest.getStartDateTime())
                .endDateTime(endDateTime)
                .build();

        this.reportRepository.save(report); // 리포트 등록(계산된 점수 회원 총점에 누적되며 배지 세팅, 식단 저장됨)
    }

    /** 절약 금액 로직 */
    private void accumulateSavingsAndRecommend(Member writer, HealthKit healthKit) {
        int prevTotalSavedMoney = writer.getTotalSavedMoney();
        writer.accumulateSmokingSavedMoneyForDay(healthKit.getSmokedCigarettes());
        writer.accumulateAlcoholSavedMoneyForDay(healthKit.getAlcoholConsumptionResult().orElse(null));

        int curTotalSavedMoney = writer.getTotalSavedMoney();
        if (curTotalSavedMoney < MIN_SAVED_MONEY_REQUIRED) {
            writer.updateHealthItemsRecommend(SAVED_MONEY_DEFAULT_COMPLETION.get());
        } else if (curTotalSavedMoney != prevTotalSavedMoney) {
            PromptCompletion healthItemsRecommend = gptService.call(new HealthItemsRecommendPrompt(writer, healthKit.getWorkouts()));
            writer.updateHealthItemsRecommend(healthItemsRecommend);
        }
    }

    /** 식단 기록 요청 VO를 바탕으로 식단 리스트 생성 */
    private List<Meal> createMeals(MealsRequest mealsRequest) {
        Set<String> eatenFoodCodeSet = mealsRequest.getEatenFoodCodeSet();
        EatenFoodMap eatenFoodMap = new EatenFoodMap(foodRepository.findByFoodCodeIn(eatenFoodCodeSet));
        return mealsRequest.getMealRequestList().stream()
                .map(mealRequest ->
                        Meal.newInstance(eatenFoodMap, mealRequest))
                .toList();
    }

    /** 이미 해당 일자에 기록된 리포트가 있는지 확인 */
    private void validateDuplicateReport(Long memberId, LocalDate endDate) {
        Optional<HealthReport> findReport = reportRepository.findByMemberIdAndEndDate(memberId, endDate);
        if (!findReport.isEmpty()) {
            throw new DuplicateEntityException(DUPLICATED_REPORT_SUBMIT);
        }
    }

}
