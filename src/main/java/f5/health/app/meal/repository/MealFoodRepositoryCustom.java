package f5.health.app.meal.repository;

import f5.health.app.meal.entity.MealFood;

import java.util.List;

public interface MealFoodRepositoryCustom {

    int[] saveAllMealFoods(List<MealFood> mealFoods);
}
