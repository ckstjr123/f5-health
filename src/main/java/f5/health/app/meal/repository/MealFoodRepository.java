package f5.health.app.meal.repository;

import f5.health.app.meal.entity.MealFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MealFoodRepository extends JpaRepository<MealFood, Long>, MealFoodRepositoryCustom {

    @Query("SELECT mf FROM MealFood mf WHERE mf.meal.id = :id")
    List<MealFood> findByMealId(@Param("id") Long id);

    /** 식단 수정 시 호출 */
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM MealFood mf WHERE mf.id IN :ids")
    void deleteByIdIn(@Param("ids") Set<Long> ids);

    /** 식단 삭제 시 호출 */
    @Modifying
    @Query("DELETE FROM MealFood mf WHERE mf.meal.id = :id")
    void deleteByMealId(@Param("id") Long id);
}
