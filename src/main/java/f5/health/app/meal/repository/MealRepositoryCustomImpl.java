package f5.health.app.meal.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import f5.health.app.meal.domain.MealType;
import f5.health.app.meal.domain.Meal;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static f5.health.app.food.entity.QFood.food;
import static f5.health.app.meal.domain.QMeal.meal;
import static f5.health.app.meal.domain.QMealFood.mealFood;

public class MealRepositoryCustomImpl implements MealRepositoryCustom {

    private final JPAQueryFactory query;

    public MealRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    @Override
    public long countBy(Long memberId, LocalDate eatenDate, MealType mealType) {
        return query
                .select(meal.count())
                .from(meal)
                .where(meal.member.id.eq(memberId),
                        meal.eatenDate.eq(eatenDate),
                        meal.mealType.eq(mealType))
                .fetchOne();
    }

    @Override
    public Optional<Meal> findMealJoinFetch(Long mealId) {
        return Optional.ofNullable(
                query.selectFrom(meal)
                     .join(meal.mealFoods, mealFood).fetchJoin()
                     .join(mealFood.food, food).fetchJoin()
                     .where(meal.id.eq(mealId))
                     .fetchOne());
    }

    @Override
    public List<Meal> findMeals(Long memberId, LocalDate eatenDate) {
        return query.selectFrom(meal)
                    .where(
                            meal.member.id.eq(memberId),
                            meal.eatenDate.eq(eatenDate))
                    .fetch();
    }
}
