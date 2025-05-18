package f5.health.app.service.healthreport;

import f5.health.app.entity.HealthReport;
import f5.health.app.entity.Member;
import f5.health.app.entity.meal.EatenFoodMap;
import f5.health.app.entity.meal.Meal;
import f5.health.app.exception.global.DuplicateEntityException;
import f5.health.app.exception.global.NotFoundException;
import f5.health.app.repository.HealthReportRepository;
import f5.health.app.service.food.FoodService;
import f5.health.app.service.healthreport.openai.GptService;
import f5.health.app.service.healthreport.openai.prompt.HealthFeedbackPrompt;
import f5.health.app.service.healthreport.openai.prompt.HealthItemsRecommendPrompt;
import f5.health.app.service.healthreport.vo.request.HealthReportRequest;
import f5.health.app.service.healthreport.vo.request.MealsRequest;
import f5.health.app.service.healthreport.vo.request.NutritionFacts;
import f5.health.app.service.healthreport.vo.request.healthkit.HealthKit;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.Workouts;
import f5.health.app.service.member.MemberService;
import f5.health.app.vo.healthreport.response.HealthReportResponse;
import f5.health.app.vo.openai.response.PromptCompletion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static f5.health.app.exception.healthreport.HealthReportErrorCode.DUPLICATED_REPORT_SUBMIT;
import static f5.health.app.exception.healthreport.HealthReportErrorCode.NOT_FOUND_REPORT;
import static f5.health.app.exception.member.MemberErrorCode.NOT_FOUND_MEMBER;
import static f5.health.app.service.healthreport.openai.prompt.DEFAULT_COMPLETION.SAVED_MONEY_DEFAULT_COMPLETION;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthReportService {

    public static final int MINIMUM_SAVED_MONEY_REQUIRED = 5000;
    private final MemberService memberService;
    private final FoodService foodService;
    private final GptService gptService;
    private final HealthLifeStyleScoreCalculator healthLifeStyleScoreCalculator;
    private final HealthReportRepository reportRepository;

    /** 리포트 조회 */
    @Transactional(readOnly = true)
    public HealthReportResponse findReport(Long memberId, LocalDate endDate) {
        HealthReport report = reportRepository.findByMemberIdAndEndDate(memberId, endDate)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_REPORT));
        return new HealthReportResponse(report, report.getMeals());
    }


    /** 리포트 등록 */
    @Transactional
    public HealthReportResponse submit(Long memberId, HealthReportRequest reportRequest) {
        LocalDateTime endDateTime = reportRequest.getEndDateTime();
        this.validateDuplicateReport(memberId, endDateTime.toLocalDate());

        // 절약 금액 로직
        HealthKit healthKit = reportRequest.getHealthKit();
        Member writer = memberService.findById(memberId).orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));
        this.accumulateSavedMoney(writer, healthKit);

        // 식단
        List<Meal> meals = this.createMeals(reportRequest.getMealsRequest());
        NutritionFacts nutritionFacts = NutritionFacts.from(meals);

        // 리포트 저장(계산된 점수가 회원 총점에 누적되고 배지 세팅됨)
        HealthReport report = HealthReport.builder(writer, meals)
                .healthLifeScore(this.healthLifeStyleScoreCalculator.calculateScore(writer, healthKit, nutritionFacts))
                .waterIntake(healthKit.getWaterIntake())
                .smokeCigarettes(healthKit.getSmokedCigarettes())
                .alcoholDrinks(healthKit.getConsumedAlcoholDrinks())
                .healthFeedback(gptService.call(new HealthFeedbackPrompt(writer, healthKit, nutritionFacts)))
                .startDateTime(reportRequest.getStartDateTime())
                .endDateTime(endDateTime)
                .build();

        this.reportRepository.save(report);
        return new HealthReportResponse(report, meals);
    }

    private void accumulateSavedMoney(Member writer, HealthKit healthKit) {
        writer.accumulateSmokingSavedMoneyForDay(healthKit.getSmokedCigarettes());
        writer.accumulateAlcoholSavedMoneyForDay(healthKit.getConsumedAlcoholDrinks());
        this.recommendHealthItems(writer, healthKit.getWorkouts());
    }

    /** 절약 금액이 일정 금액 이상이면 gpt 건강 물품 피드백 요청, 일정 금액 미만이면 디폴트 메시지 */
    private void recommendHealthItems(Member writer, Workouts workouts) {
        int totalSavedMoney = writer.getTotalSavedMoney();
        PromptCompletion HealthItemsRecommend = (MINIMUM_SAVED_MONEY_REQUIRED <= totalSavedMoney) ?
                gptService.call(new HealthItemsRecommendPrompt(writer, workouts)) : SAVED_MONEY_DEFAULT_COMPLETION.get();

        writer.updateHealthItemsRecommend(HealthItemsRecommend); //
    }

    /** 식단 기록 요청 VO를 바탕으로 식단 리스트 생성 */
    private List<Meal> createMeals(MealsRequest mealsRequest) {
        EatenFoodMap eatenFoodMap = foodService.findFoodsBy(mealsRequest.getEatenFoodCodeSet());
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
