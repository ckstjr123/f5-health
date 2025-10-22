package f5.health.app.activity.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import f5.health.app.activity.domain.Activity;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.Optional;

import static f5.health.app.activity.domain.QActivity.activity;
import static f5.health.app.activity.domain.QAlcoholConsumption.alcoholConsumption;

public class ActivityRepositoryCustomImpl implements ActivityRepositoryCustom {

    private final JPAQueryFactory query;

    public ActivityRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    @Override
    public Optional<Activity> findActivityJoinFetch(Long memberId, LocalDate recordDate) {
        return Optional.ofNullable(
                query.selectFrom(activity)
                     .leftJoin(activity.alcoholConsumptions, alcoholConsumption).fetchJoin()
                     .where(
                             activity.member.id.eq(memberId),
                             activity.recordDate.eq(recordDate)
                     )
                     .fetchOne());
    }
}
