package f5.health.app.meal.repository;

import f5.health.app.meal.entity.MealFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MealFoodRepository extends JpaRepository<MealFood, Long>, MealFoodRepositoryCustom {

    @Query("SELECT mf FROM MealFood mf WHERE mf.meal.id = :id")
    List<MealFood> findByMealId(@Param("id") Long id);
}
