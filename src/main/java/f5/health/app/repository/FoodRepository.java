package f5.health.app.repository;

import f5.health.app.entity.Food;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface FoodRepository extends JpaRepository<Food, String> {

    /** WHERE IN ("foodCode", ...) */
    List<Food> findByFoodCodeIn(@Param("foodCodeSet") Set<String> foodCodeSet);

    /** 음식 검색용 */
    List<Food> findByFoodNameLike(@Param("foodName") String foodName, Pageable pageable);
}
