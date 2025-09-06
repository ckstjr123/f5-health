package f5.health.app.meal.repository;

import f5.health.app.meal.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long>, MealRepositoryCustom {
}
