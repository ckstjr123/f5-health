package f5.health.app.meal.repository;

import f5.health.app.meal.domain.MealFood;

import java.util.List;

public interface MealFoodRepositoryCustom {

    int[] saveAllBatch(List<MealFood> mealFoods);
}
