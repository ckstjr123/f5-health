package f5.health.app.activity.repository;

import f5.health.app.activity.domain.Activity;

import java.time.LocalDate;
import java.util.Optional;

public interface ActivityRepositoryCustom {

    Optional<Activity> findActivityJoinFetch(Long memberId, LocalDate recordedDate);
}
