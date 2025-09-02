package f5.health.app.meal.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.entity.Meal;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static f5.health.app.meal.entity.QMeal.meal;

public class MealRepositoryCustomImpl implements MealRepositoryCustom {

    private final JPAQueryFactory query;

    public MealRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    @Override
    public Optional<Meal> findOne(Long memberId, LocalDate eatenDate, MealType mealType) {
        return Optional.ofNullable(
                query.selectFrom(meal)
                        .where(meal.member.id.eq(memberId),
                                meal.eatenDate.eq(eatenDate),
                                meal.mealType.eq(mealType))
                        .fetchOne());
    }

    @Override
    public List<Meal> findAll(Long memberId, LocalDate eatenDate) {
        return List.of();
    }
}
