package f5.health.app.entity;

import f5.health.app.constant.MealType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** 식단(식사) 엔티티 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MEAL")
public class Meal {

    public static final int MENU_LIMIT_SIZE_PER_MEAL = 10; // 식사당 등록 가능한 메뉴 최대 개수

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HEALTH_REPORT_ID")
    private HealthReport report;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL) //양방향
    private List<MealFood> mealFoods = new ArrayList<>();

    @Column(name = "MEAL_TYPE")
    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @Column(name = "MEAL_TIME")
    private LocalDateTime mealTime;

    @Column(name = "MEAL_CALORIES")
    private int mealCalories; // 계산된 식사 총 칼로리
}
