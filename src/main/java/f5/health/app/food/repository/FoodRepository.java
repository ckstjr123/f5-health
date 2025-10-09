package f5.health.app.food.repository;

import f5.health.app.food.entity.Food;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface FoodRepository extends JpaRepository<Food, Long> {
    /** 음식 검색 */
    List<Food> findByFoodNameLike(String foodName, Pageable pageable);
}
