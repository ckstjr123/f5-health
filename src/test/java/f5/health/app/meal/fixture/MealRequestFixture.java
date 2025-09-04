package f5.health.app.meal.fixture;

import f5.health.app.food.entity.Food;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.service.request.MealFoodRequest;
import f5.health.app.meal.service.request.MealRequest;

import java.time.LocalDateTime;
import java.util.List;

public class MealRequestFixture {

    public static MealRequest createMealRequest(MealType mealType, LocalDateTime eatenAt, List<Food> foods) {
        List<MealFoodRequest> mealFoodRequestList = foods.stream()
                .map(food -> new MealFoodRequest(food.getFoodCode(), 1.0))
                .toList();

        return new MealRequest(mealFoodRequestList, mealType, eatenAt);
    }

}
