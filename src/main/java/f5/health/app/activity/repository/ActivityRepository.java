package f5.health.app.activity.repository;

import f5.health.app.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    /** 해당 일자에 기록된 특정 회원의 리포트 조회 */
    Optional<Activity> findByMemberIdAndRecordDate(Long memberId, LocalDate recordDate);
}
