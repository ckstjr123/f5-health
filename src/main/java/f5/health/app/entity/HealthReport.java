package f5.health.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** 건강 리포트 엔티티 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "HEALTH_REPORT", uniqueConstraints = {@UniqueConstraint(columnNames = {"MEMBER_ID", "END_DATE"})})
public class HealthReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HEALTH_REPORT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "HEALTH_LIFE_SCORE")
    private int healthLifeScore;

    @Column(name = "WATER_INTAKE")
    private int waterIntake;

    @Column(name = "SMOKED_CIGARETTES")
    private int smokedCigarettes;

    @Column(name = "ALCOHOL_DRINKS")
    private int alcoholDrinks;

    @Column(name = "FITNESS_ADVICE")
    private String fitnessAdvice;

    @Column(name = "MEAL_ADVICE")
    private String mealAdvice;
    
    // startDate ~ endDate: 건강 관측 기간 //
    @Column(name = "START_DATE_TIME")
    private LocalDateTime startDateTime;

    @Column(name = "END_DATE")
    private LocalDate endDate; // 'yyyy-MM-DD' 일자별 조회 시 인덱스 성능을 높이기 위해 endTime과 분리

    @Column(name = "END_TIME")
    private LocalDate endTime; // 'HH:mm:ss'

    @Column(name = "REPORTED_AT")
    private LocalDateTime reportedAt; // 실제 작성 시각
}
