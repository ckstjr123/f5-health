package f5.health.app.session.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import f5.health.app.session.entity.Session;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static f5.health.app.member.entity.QMember.member;
import static f5.health.app.session.entity.QSession.session;

public class SessionRepositoryCustomImpl implements SessionRepositoryCustom {

    private final JPAQueryFactory query;

    public SessionRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Session> findByMemberIdJoinFetch(Long memberId) {
        return query.selectFrom(session)
                    .join(session.member, member).fetchJoin()
                    .where(member.id.eq(memberId))
                    .fetch();
    }

    @Override
    public Optional<Session> findByRefreshTokenJoinFetch(String refreshToken) {
        return Optional.ofNullable(
                query.selectFrom(session)
                     .join(session.member, member).fetchJoin()
                     .where(session.refreshToken.eq(refreshToken))
                     .fetchOne()
        );
    }

}
