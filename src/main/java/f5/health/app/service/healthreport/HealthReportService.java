package f5.health.app.service.healthreport;

import f5.health.app.entity.*;
import f5.health.app.exception.food.FoodErrorCode;
import f5.health.app.exception.global.DuplicateEntityException;
import f5.health.app.exception.global.NotFoundException;
import f5.health.app.repository.FoodRepository;
import f5.health.app.repository.HealthReportRepository;
import f5.health.app.service.healthreport.vo.request.HealthReportRequest;
import f5.health.app.service.healthreport.vo.request.MealFoodRequest;
import f5.health.app.service.healthreport.vo.request.MealsRequest;
import f5.health.app.service.member.MemberService;
import f5.health.app.vo.healthreport.response.HealthReportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static f5.health.app.exception.healthreport.HealthReportErrorCode.DUPLICATED_REPORT_SUBMIT;
import static f5.health.app.exception.member.MemberErrorCode.NOT_FOUND_MEMBER;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthReportService {

    private final MemberService memberService;
    private final FoodRepository foodRepository;
    private final HealthReportRepository reportRepository;


    /** 리포트 등록 */
    @Transactional
    public HealthReportResponse submit(Long memberId, HealthReportRequest reportRequest) {
        LocalDateTime endDateTime = reportRequest.getEndDateTime();
        this.validateDuplicateReport(memberId, endDateTime.toLocalDate());

        
        // 식단
        List<Meal> meals = this.createMeals(reportRequest.getMealsRequest());

        
        
        // 프롬포트(gpt 건강 피드백)



        // 점수 계산



        // member: , 배지 체크, 절약 금액 계산 및 gpt 건강 아이템(운동 기구) 피드백
        Member writer = memberService.findById(memberId).orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));



        // 리포트 생성(계산된 점수 회원 totalHealthLifeScore에 누적됨) 및 저장(cascade)
        HealthReport report = HealthReport.builder(writer, meals)
                .healthLifeScore(100)
                .waterIntake(250)
                .smokeCigarettes(8)
                .alcoholDrinks(5)
                .healthFeedback("gpt_health_feedback")
                .startDateTime(reportRequest.getStartDateTime())
                .endDateTime(endDateTime)
                .build();
        this.reportRepository.save(report);


        // HealthReportResponse DTO 생성
        return new HealthReportResponse();
    }


    /** 식단 기록 요청 VO를 바탕으로 식단 엔티티 리스트 생성 */
    private List<Meal> createMeals(MealsRequest mealsRequest) {
        Set<String> eatenFoodCodeSet = mealsRequest.getEatenFoodCodeSet();
        List<Food> eatenFoods = this.foodRepository.findByFoodCodeIn(eatenFoodCodeSet);

        // 각 식사에서 먹은 음식을 꺼내기 위해 음식 코드와 음식 매핑
        Map<String, Food> eatenFoodMap = eatenFoods.stream().collect(Collectors.toMap(Food::getFoodCode, Function.identity())); // Function.identity(): eatenFood -> eatenFood

        return mealsRequest.getMealRequestList().stream()
                .map(mealRequest -> {
                    List<MealFood> mealFoods = this.createMealFoods(eatenFoodMap, mealRequest.getMealFoodsRequest());
                    return Meal.newInstance(mealRequest.getMealType(), mealRequest.getMealTime(), mealFoods);
                })
                .toList();
    }

    /** 식사당 먹은 음식 및 각 수량을 나타내는 MealFoods */
    private List<MealFood> createMealFoods(final Map<String, Food> eatenFoodMap, List<MealFoodRequest> mealFoodsRequest) {
        return mealFoodsRequest.stream()
                .map(mealFoodRequest -> {
                    Food food = eatenFoodMap.get(mealFoodRequest.getFoodCode());
                    if (food == null) {
                        throw new NotFoundException(FoodErrorCode.NOT_FOUND_FOOD, mealFoodRequest.getFoodCode());
                    }
                    return MealFood.newInstance(food, mealFoodRequest.getCount());
                })
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
