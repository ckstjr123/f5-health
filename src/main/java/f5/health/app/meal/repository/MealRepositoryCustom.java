package f5.health.app.meal.repository;

import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.entity.Meal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealRepositoryCustom {

    long countBy(Long memberId, LocalDate eatenDate, MealType mealType);

    Optional<Meal> findMealJoinFetch(Long mealId);

    /** 해당 일자에 기록된 모든 식단 정보 조회 */
    List<Meal> findMeals(Long memberId, LocalDate eatenDate);
}
