package f5.health.app.service.healthreport;

import f5.health.app.entity.HealthReport;
import f5.health.app.entity.Member;
import f5.health.app.entity.meal.EatenFoodMap;
import f5.health.app.entity.meal.Meal;
import f5.health.app.exception.global.DuplicateEntityException;
import f5.health.app.exception.global.NotFoundException;
import f5.health.app.repository.HealthReportRepository;
import f5.health.app.service.food.FoodService;
import f5.health.app.service.healthreport.vo.request.HealthReportRequest;
import f5.health.app.service.healthreport.vo.request.MealsRequest;
import f5.health.app.service.healthreport.vo.request.NutritionFacts;
import f5.health.app.service.healthreport.vo.request.healthkit.HealthKit;
import f5.health.app.service.member.MemberService;
import f5.health.app.service.openai.GptService;
import f5.health.app.service.openai.HealthFeedbackPrompt;
import f5.health.app.vo.healthreport.response.HealthReportResponse;
import f5.health.app.vo.meal.response.MealResponse;
import f5.health.app.vo.meal.response.MealsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static f5.health.app.exception.healthreport.HealthReportErrorCode.DUPLICATED_REPORT_SUBMIT;
import static f5.health.app.exception.member.MemberErrorCode.NOT_FOUND_MEMBER;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthReportService {

    private final MemberService memberService;
    private final FoodService foodService;
    private final GptService gptService;
    private final HealthLifeStyleScoreCalculator healthLifeStyleScoreCalculator;
    private final HealthReportRepository reportRepository;


    /** 리포트 등록 */
    @Transactional
    public HealthReportResponse submit(Long memberId, HealthReportRequest reportRequest) {
        LocalDateTime endDateTime = reportRequest.getEndDateTime();
        this.validateDuplicateReport(memberId, endDateTime.toLocalDate());

        // 식단
        List<Meal> meals = this.createMeals(reportRequest.getMealsRequest());
        MealsResponse mealsResponse = MealsResponse.from(meals.stream()
                .map(meal -> MealResponse.only(meal))
                .toList());

        // 점수 계산
        HealthKit healthKit = reportRequest.getHealthKit();
        NutritionFacts nutritionFacts = NutritionFacts.from(meals);
        int healthLifeScore = healthLifeStyleScoreCalculator.calculateScore(healthKit, nutritionFacts);

        // member: 배지 체크, 절약 금액 계산 뒤 절약 금액이 '일정 금액 이상일 때만' gpt 건강 아이템(운동 기구) 피드백
        // 절약금액이 일정 금액보다 낮으면 디폴트 메시지로 업데이트
        Member writer = memberService.findById(memberId).orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));
//        writer.accumulateSmokingSavedMoneyForDay();
//        writer.accumulateAlcoholSavedMoneyForDay();

        // 리포트 생성(계산된 점수 회원 totalHealthLifeScore에 누적됨, 배지 체크 로직 추가 필요) 후 저장(cascade)
        HealthReport report = HealthReport.builder(writer, meals)
                .healthLifeScore(healthLifeScore)
                .waterIntake(healthKit.getWaterIntake())
                .smokeCigarettes(healthKit.getSmokedCigarettes())
                .alcoholDrinks(healthKit.getConsumedAlcoholDrinks())
                .healthFeedback(gptService.call(new HealthFeedbackPrompt(healthKit, nutritionFacts)))
                .startDateTime(reportRequest.getStartDateTime())
                .endDateTime(endDateTime)
                .build();
        this.reportRepository.save(report);

        // HealthReportResponse DTO 생성
        return new HealthReportResponse();
    }


    /** 식단 기록 요청 VO를 바탕으로 식단 엔티티 리스트 생성 */
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
