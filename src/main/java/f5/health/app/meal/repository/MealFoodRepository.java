package f5.health.app.meal.repository;

import f5.health.app.meal.entity.MealFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MealFoodRepository extends JpaRepository<MealFood, Long>, MealFoodRepositoryCustom {

    @Modifying // (clearAutomatically = true)
    @Query("DELETE FROM MealFood mf WHERE mf.meal.id = :mealId")
    void deleteByMealId(@Param("mealId") Long mealId);
}
