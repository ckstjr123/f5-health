package f5.health.app.meal.repository;

import f5.health.app.meal.domain.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long>, MealRepositoryCustom {
}
