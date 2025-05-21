package f5.health.app.repository;

import f5.health.app.entity.healthreport.HealthReport;
import f5.health.app.vo.member.response.HealthLifeScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HealthReportRepository extends JpaRepository<HealthReport, Long> {

    /** 해당 일자에 기록된 특정 회원의 리포트 조회 */
    Optional<HealthReport> findByMemberIdAndEndDate(Long memberId, LocalDate endDate);

    List<HealthReport> findByMemberIdAndEndDateBetween(Long memberId, LocalDate start, LocalDate end);

    List<HealthLifeScore> findScoresByMemberIdAndEndDateBetween(Long memberId, LocalDate start, LocalDate end);
}
