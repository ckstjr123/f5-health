package f5.health.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** 건강 일지 엔티티 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "HEALTH_REPORT", uniqueConstraints = {@UniqueConstraint(columnNames = {"MEMBER_ID", "END_DATE"})})
public class HealthReport {

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


    /** HealthReport ↔ Meal 양방향 매핑 */
    public void addAllMeals(List<Meal> meals) {
        this.meals.addAll(meals);
        for (Meal meal : meals) {
            meal.setReport(this);
        }
    }
}
