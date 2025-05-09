package f5.health.app.repository;

import f5.health.app.entity.Food;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, String> {
    /** 음식 검색용 */ // foodRepository.findByFoodNameLike(foodSearchQuery + "%", PageRequest.of(0, 15));
    List<Food> findByFoodNameLike(String foodName, Pageable pageable);
}
