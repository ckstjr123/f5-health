package f5.health.app.meal.service;

import f5.health.app.food.entity.Food;
import f5.health.app.food.fixture.FoodFixture;
import f5.health.app.food.repository.FoodRepository;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.entity.Meal;
import f5.health.app.meal.repository.MealRepository;
import f5.health.app.meal.service.request.MealFoodRequest;
import f5.health.app.meal.service.request.MealRequest;
import f5.health.app.member.entity.Member;
import f5.health.app.member.fixture.MemberFixture;
import f5.health.app.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static f5.health.app.meal.fixture.MealRequestFixture.createMealRequest;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Transactional
@SpringBootTest
public class MealServiceCommandTest {

    private static final int ZERO_INDEX = 0;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private MealService mealService;


    @DisplayName("식사한 음식이 담긴 식단 저장")
    @Test
    void save() {
        Member member = createMember();
        List<Food> foods = createFoods();
        LocalDateTime eatenAt = LocalDateTime.now();
        MealType mealType = MealType.LUNCH;
        MealRequest mealRequest = createMealRequest(mealType, eatenAt, foods);

        Long mealId = mealService.save(member.getId(), mealRequest);

        Meal meal = mealRepository.findById(mealId).orElseThrow();
        List<MealFoodRequest> mealFoodRequestList = mealRequest.getMealFoodRequestList();
        assertSoftly(s -> {
            s.assertThat(meal.getId()).isEqualTo(mealId);
            s.assertThat(meal.getEatenDate()).isEqualTo(eatenAt.toLocalDate());
            s.assertThat(meal.getMealFoods().size()).isEqualTo(foods.size());
            s.assertThat(meal.getTotalKcal()).isEqualTo(getTotalKcal(foods, mealFoodRequestList));
            s.assertThat(meal.getTotalCarbohydrate()).isEqualTo(getTotalCarbohydrate(foods, mealFoodRequestList));
            s.assertThat(meal.getTotalProtein()).isEqualTo(getTotalProtein(foods, mealFoodRequestList));
            s.assertThat(meal.getTotalFat()).isEqualTo(getTotalFat(foods, mealFoodRequestList));
        });
    }


    private Member createMember() {
        return memberRepository.save(MemberFixture.createMember());
    }

    private List<Food> createFoods() {
        List<Food> foods = List.of(
                FoodFixture.createBasicFood("G123-226020200-1391", "김밥"),
                FoodFixture.createHighProteinFood("G123-226020200-1392", "요거트"),
                FoodFixture.createHighCalorieFood("G123-226020200-1393", "떡볶이"),
                FoodFixture.createLowSugarFood("G123-226020200-1394", "제로 콜라")
        );
        return foodRepository.saveAll(foods);
    }

    private int getTotalKcal(List<Food> foods, List<MealFoodRequest> mealFoodRequestList) {
        return IntStream.range(ZERO_INDEX, foods.size())
                .map(i -> (int) (foods.get(i).getKcal() * mealFoodRequestList.get(i).getCount()))
                .sum();
    }

    private double getTotalCarbohydrate(List<Food> foods, List<MealFoodRequest> mealFoodRequestList) {
        return IntStream.range(ZERO_INDEX, foods.size())
                .mapToDouble(i -> foods.get(i).getCarbohydrate() * mealFoodRequestList.get(i).getCount())
                .sum();
    }

    private double getTotalProtein(List<Food> foods, List<MealFoodRequest> mealFoodRequestList) {
        return IntStream.range(ZERO_INDEX, foods.size())
                .mapToDouble(i -> foods.get(i).getProtein() * mealFoodRequestList.get(i).getCount())
                .sum();
    }

    private double getTotalFat(List<Food> foods, List<MealFoodRequest> mealFoodRequestList) {
        return IntStream.range(ZERO_INDEX, foods.size())
                .mapToDouble(i -> foods.get(i).getFat() * mealFoodRequestList.get(i).getCount())
                .sum();
    }

}
