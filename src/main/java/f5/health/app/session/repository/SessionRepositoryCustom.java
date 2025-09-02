package f5.health.app.session.repository;

import f5.health.app.session.entity.Session;

import java.util.List;
import java.util.Optional;

public interface SessionRepositoryCustom {

    Optional<Session> findByRefreshTokenJoinFetch(String refreshToken);

    List<Session> findByMemberIdJoinFetch(Long memberId);
}
