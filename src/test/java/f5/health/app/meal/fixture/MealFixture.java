package f5.health.app.meal.fixture;

import f5.health.app.food.fixture.FoodFixture;
import f5.health.app.food.entity.Food;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.entity.Meal;
import f5.health.app.meal.entity.MealFood;
import f5.health.app.member.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MealFixture {

    public static Meal createMealOnly(Member member, LocalDateTime eatenAt, MealType mealType) {
        return Meal.newInstance(member, eatenAt, mealType, new ArrayList<>());
    }

    public static Meal createMealWithMealFoods(Member member, LocalDateTime eatenAt, MealType mealType) {
        Food food1 = FoodFixture.createBasicFood("D320-716042200-0000", "된장찌개");
        Food food2 = FoodFixture.createLowCalorieFood("D312-550020000-0001", "현미밥");

        return Meal.newInstance(
                member,
                eatenAt,
                mealType,
                List.of(
                        MealFood.newInstance(food1, 0.5),
                        MealFood.newInstance(food2, 1.0)
                )
        );
    }

    public static List<Meal> createMealsWithMealFoods(Member member, LocalDateTime eatenAt) {
        Food food1 = FoodFixture.createBasicFood("D402-145000000-0001", "김치찌개");
        Food food2 = FoodFixture.createHighProteinFood("D402-145000000-0002", "닭가슴살 샐러드");
        Food food3 = FoodFixture.createHighCalorieFood("D402-145000000-0003", "치즈 돈까스");
        Food food4 = FoodFixture.createLowCalorieFood("D402-145000000-0004", "국수");
        Food food5 = FoodFixture.createLowSugarFood("D402-145000000-0005", "저당 음료");
        Food food6 = FoodFixture.createHighProteinFood("D402-145000000-0006", "케이크");
        Food food7 = FoodFixture.createBasicFood("D402-145000000-0007", "잡곡밥");
        Food food8 = FoodFixture.createLowSugarFood("D402-145000000-0008", "물냉면");

        return List.of(
                Meal.newInstance(
                        member,
                        eatenAt,
                        MealType.BREAKFAST,
                        List.of(MealFood.newInstance(food2, 1.5))
                ),
                Meal.newInstance(
                        member,
                        eatenAt,
                        MealType.LUNCH,
                        List.of(
                                MealFood.newInstance(food1, 1.0),
                                MealFood.newInstance(food7, 0.8),
                                MealFood.newInstance(food8, 0.5)
                        )
                ),
                Meal.newInstance(
                        member,
                        eatenAt,
                        MealType.DINNER,
                        List.of(
                                MealFood.newInstance(food3, 1.0),
                                MealFood.newInstance(food4, 2.0)
                        )
                ),
                Meal.newInstance(
                        member,
                        eatenAt,
                        MealType.SNACK,
                        List.of(
                                MealFood.newInstance(food5, 0.2),
                                MealFood.newInstance(food6, 0.7)
                        )
                )
        );
    }

}
