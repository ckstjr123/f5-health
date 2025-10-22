package f5.health.app.meal.service;

import f5.health.app.food.entity.Food;
import f5.health.app.food.fixture.FoodFixture;
import f5.health.app.food.repository.FoodRepository;
import f5.health.app.meal.domain.MealType;
import f5.health.app.meal.domain.Meal;
import f5.health.app.meal.domain.MealFood;
import f5.health.app.meal.domain.embedded.Nutrition;
import f5.health.app.meal.fixture.MealFixture;
import f5.health.app.meal.repository.MealRepository;
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

import static f5.health.app.meal.fixture.MealRequestFixture.createMealRequest;
import static f5.health.app.meal.fixture.MealRequestFixture.toMealFoodParams;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
public class MealServiceIntegrationTest {

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

        List<MealFood> mealFoodFixtures = MealFixture.createMealFoods(foods);
        MealRequest mealRequest = createMealRequest(MealType.LUNCH, eatenAt, toMealFoodParams(mealFoodFixtures));

        Long mealId = mealService.saveMeal(member.getId(), mealRequest);

        Meal meal = mealRepository.findById(mealId).orElseThrow();

        Nutrition expectedNutrition = Nutrition.from(mealFoodFixtures);
        Nutrition nutrition = meal.getNutrition();
        assertAll(
                () -> assertThat(meal.getId()).isEqualTo(mealId),
                () -> assertThat(meal.getEatenDate()).isEqualTo(eatenAt.toLocalDate()),
                () -> assertThat(meal.getMealFoods().size()).isEqualTo(foods.size()),
                () -> assertThat(nutrition.getKcal()).isEqualTo(expectedNutrition.getKcal()),
                () -> assertThat(nutrition.getCarbohydrate()).isEqualTo(expectedNutrition.getCarbohydrate()),
                () -> assertThat(nutrition.getProtein()).isEqualTo(expectedNutrition.getProtein()),
                () -> assertThat(nutrition.getFat()).isEqualTo(expectedNutrition.getFat())
        );
    }

    private Member createMember() {
        return memberRepository.save(MemberFixture.createMember());
    }

    private List<Food> createFoods() {
        return foodRepository.saveAll(FoodFixture.createFoods());
    }

}
