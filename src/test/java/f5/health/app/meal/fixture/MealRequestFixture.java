package f5.health.app.meal.fixture;

import f5.health.app.food.entity.Food;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.service.request.MealFoodParam;
import f5.health.app.meal.service.request.MealRequest;

import java.time.LocalDateTime;
import java.util.List;

public class MealRequestFixture {

    public static MealRequest createMealRequest(MealType mealType, LocalDateTime eatenAt, List<Food> foods) {
        List<MealFoodParam> mealFoodParams = foods.stream()
                .map(food -> new MealFoodParam(food.getFoodCode(), 1.0))
                .toList();

        return new MealRequest(mealFoodParams, mealType, eatenAt);
    }

}
