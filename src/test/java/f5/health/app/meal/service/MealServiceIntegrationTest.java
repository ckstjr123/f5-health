package f5.health.app.meal.service;

import f5.health.app.food.entity.Food;
import f5.health.app.food.fixture.FoodFixture;
import f5.health.app.food.repository.FoodRepository;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.entity.Meal;
import f5.health.app.meal.repository.MealRepository;
import f5.health.app.meal.service.request.MealFoodParam;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
public class MealServiceIntegrationTest {

    private static final int ZERO_INDEX = 0;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private MealService mealService;


    @DisplayName("식사한 음식이 기록된 식단 저장")
    @Test
    void saveMeal() {
        Member member = createMember();
        List<Food> foods = createFoods();
        LocalDateTime eatenAt = LocalDateTime.now();
        MealRequest mealRequest = createMealRequest(MealType.LUNCH, eatenAt, foods);

        Long mealId = mealService.saveMeal(member.getId(), mealRequest);

        Meal meal = mealRepository.findById(mealId).orElseThrow();
        List<MealFoodParam> mealFoodParams = mealRequest.mealFoodParams();
        assertAll(
                () -> assertThat(meal.getId()).isEqualTo(mealId),
                () -> assertThat(meal.getEatenDate()).isEqualTo(eatenAt.toLocalDate()),
                () -> assertThat(meal.getMealFoods().size()).isEqualTo(foods.size()),
                () -> assertThat(meal.getTotalKcal()).isEqualTo(getTotalKcal(foods, mealFoodParams)),
                () -> assertThat(meal.getTotalCarbohydrate()).isEqualTo(getTotalCarbohydrate(foods, mealFoodParams)),
                () -> assertThat(meal.getTotalProtein()).isEqualTo(getTotalProtein(foods, mealFoodParams)),
                () -> assertThat(meal.getTotalFat()).isEqualTo(getTotalFat(foods, mealFoodParams))
        );
    }


    private Member createMember() {
        return memberRepository.save(MemberFixture.createMember());
    }

    private List<Food> createFoods() {
        return foodRepository.saveAll(FoodFixture.createFoods());
    }

    private int getTotalKcal(List<Food> foods, List<MealFoodParam> mealFoodParams) {
        return IntStream.range(ZERO_INDEX, foods.size())
                .map(i -> (int) (foods.get(i).getKcal() * mealFoodParams.get(i).count()))
                .sum();
    }

    private double getTotalCarbohydrate(List<Food> foods, List<MealFoodParam> mealFoodParams) {
        return IntStream.range(ZERO_INDEX, foods.size())
                .mapToDouble(i -> foods.get(i).getCarbohydrate() * mealFoodParams.get(i).count())
                .sum();
    }

    private double getTotalProtein(List<Food> foods, List<MealFoodParam> mealFoodParams) {
        return IntStream.range(ZERO_INDEX, foods.size())
                .mapToDouble(i -> foods.get(i).getProtein() * mealFoodParams.get(i).count())
                .sum();
    }

    private double getTotalFat(List<Food> foods, List<MealFoodParam> mealFoodParams) {
        return IntStream.range(ZERO_INDEX, foods.size())
                .mapToDouble(i -> foods.get(i).getFat() * mealFoodParams.get(i).count())
                .sum();
    }

}
