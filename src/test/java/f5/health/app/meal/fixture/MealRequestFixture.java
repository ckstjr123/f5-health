package f5.health.app.meal.fixture;

import f5.health.app.meal.domain.MealType;
import f5.health.app.meal.domain.MealFood;
import f5.health.app.meal.service.request.MealFoodParam;
import f5.health.app.meal.service.request.MealRequest;

import java.time.LocalDateTime;
import java.util.List;

public class MealRequestFixture {

    public static MealRequest createMealRequest(MealType mealType, LocalDateTime eatenAt, List<MealFoodParam> mealFoodParams) {
        return new MealRequest(mealFoodParams, mealType, eatenAt);
    }

    public static List<MealFoodParam> toMealFoodParams(List<MealFood> mealFoods) {
        return mealFoods.stream()
                .map(mf -> new MealFoodParam(mf.getFood().getId(), mf.getCount()))
                .toList();
    }
}
