package f5.health.app.activity.repository;

import f5.health.app.activity.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long>, ActivityRepositoryCustom {

    Optional<Activity> findByMemberIdAndRecordedDate(Long memberId, LocalDate recordedDate);
}
