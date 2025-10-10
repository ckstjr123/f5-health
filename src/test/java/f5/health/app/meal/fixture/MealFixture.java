package f5.health.app.meal.fixture;

import f5.health.app.food.fixture.FoodFixture;
import f5.health.app.food.entity.Food;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.domain.Meal;
import f5.health.app.meal.domain.MealFood;
import f5.health.app.member.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class MealFixture {

    public static Meal createMealOnly(Member member, LocalDateTime eatenAt, MealType mealType) {
        return Meal.createMeal(member, eatenAt, mealType, new ArrayList<>());
    }

    public static List<MealFood> createMealFoods(List<Food> foods) {
        SplittableRandom random = new SplittableRandom();
        return foods.stream()
                .map(food -> MealFood.of(food, random.nextDouble(0.1, 100.1)))
                .toList();
    }

    public static Meal createMealWithMealFoods(Member member, LocalDateTime eatenAt, MealType mealType) {
        Food food1 = FoodFixture.createBasicFood("된장찌개");
        Food food2 = FoodFixture.createLowCalorieFood("현미밥");

        SplittableRandom random = new SplittableRandom();
        return Meal.createMeal(
                member,
                eatenAt,
                mealType,
                List.of(
                        MealFood.of(food1, random.nextDouble(0.1, 100.1)),
                        MealFood.of(food2, random.nextDouble(0.1, 100.1))
                )
        );
    }

}
