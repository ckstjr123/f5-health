package f5.health.app.repository;

import f5.health.app.entity.meal.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}
