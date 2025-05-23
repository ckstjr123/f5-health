package f5.health.app.entity.healthreport;

import f5.health.app.entity.Member;
import f5.health.app.entity.meal.Meal;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * 건강 일지 엔티티
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "HEALTH_REPORT", uniqueConstraints = {@UniqueConstraint(columnNames = {"MEMBER_ID", "END_DATE"})})
public class HealthReport {

    private static final int MIN_HEALTH_LIFE_SCORE = 0;
    private static final int MAX_HEALTH_LIFE_SCORE = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HEALTH_REPORT_ID")
    private Long id;

    /** 일지 작성자 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    /** 일지에 기록된 식단 */
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<Meal> meals = new ArrayList<>();

    @Column(name = "HEALTH_LIFE_SCORE")
    private int healthLifeScore;

    @Column(name = "WATER_INTAKE")
    private int waterIntake;

    @Column(name = "SMOKE_CIGARETTES")
    private int smokeCigarettes;

    @Column(name = "ALCOHOL_DRINKS")
    private int alcoholDrinks;

    @Column(name = "HEALTH_FEEDBACK")
    private String healthFeedback; // GPT 건강 피드백 결과

    // startDateTime ~ endDateTime: 건강 관측 기간 //
    @Column(name = "START_DATE_TIME")
    private LocalDateTime startDateTime;

    @Column(name = "END_DATE")
    private LocalDate endDate; // 'yyyy-MM-DD' 일자별 조회 시 인덱스 활용을 위해 endTime과 분리

    @Column(name = "END_TIME")
    private LocalTime endTime; // 'HH:mm'

    @Column(name = "REPORTED_AT")
    private LocalDateTime reportedAt;

    public static HealthReportBuilder builder(Member writer, List<Meal> meals) {
        return new HealthReportBuilder(writer, meals);
    }
    

    /** HealthReport ↔ Meal 양방향 매핑 */
    public void addAllMeals(List<Meal> meals) {
        this.meals.addAll(meals);
        for (Meal meal : meals) {
            meal.setReport(this);
        }
    }

    private void validateHealthLifeScore(int score) {
        if (score < MIN_HEALTH_LIFE_SCORE || MAX_HEALTH_LIFE_SCORE < score) {
            throw new IllegalArgumentException("score must be between ["
                    + MIN_HEALTH_LIFE_SCORE + "] and [" + MAX_HEALTH_LIFE_SCORE + "]. rejected int value: [" + score + "].");
        }
    }


    /** 리포트 빌더 */
    public static class HealthReportBuilder {

        private final HealthReport report;

        private HealthReportBuilder(Member writer, List<Meal> meals) {
            this.report = new HealthReport();
            report.reportedAt = LocalDateTime.now();
            report.member = writer;
            report.addAllMeals(meals);
        }

         /** 계산된 스코어 기록, 회원 총점에 스코어 누적 및 배지 체크 */
         public HealthReportBuilder healthLifeScore(final int score) {
            report.validateHealthLifeScore(score);
            report.healthLifeScore = score;
            report.member.addHealthLifeScore(score); //
            return this;
        }

        public HealthReportBuilder waterIntake(final int waterIntake) {
            report.waterIntake = waterIntake;
            return this;
        }

        public HealthReportBuilder smokeCigarettes(final int smokedCigarettes) {
            report.smokeCigarettes = smokedCigarettes;
            return this;
        }

        public HealthReportBuilder alcoholDrinks(final int consumedAlcoholDrinks) {
            report.alcoholDrinks = consumedAlcoholDrinks;
            return this;
        }

        public HealthReportBuilder healthFeedback(final PromptCompletion healthFeedback) {
            report.healthFeedback = healthFeedback.getContent();
            return this;
        }

        public HealthReportBuilder startDateTime(final LocalDateTime startDateTime) {
            report.startDateTime = startDateTime.truncatedTo(MINUTES);
            return this;
        }

        public HealthReportBuilder endDateTime(final LocalDateTime endDateTime) {
            report.endDate = endDateTime.toLocalDate();
            report.endTime = endDateTime.toLocalTime().truncatedTo(MINUTES);
            return this;
        }

        public HealthReport build() {
            return this.report;
        }
    }
}
