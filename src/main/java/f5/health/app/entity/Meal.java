package f5.health.app.entity;

import f5.health.app.constant.meal.MealType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** 식단(식사) 엔티티 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEAL")
public class Meal {

    public static final int MENU_LIMIT_SIZE_PER_MEAL = 10; // 식사당 등록 가능한 메뉴 최대 개수
    public static final int MEAL_TYPE_COUNT = 4; // 식사 분류 개수(MealType.values().length)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //양방향 연관 관계
    @JoinColumn(name = "HEALTH_REPORT_ID")
    private HealthReport report;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    private List<MealFood> mealFoods = new ArrayList<>();

    @Column(name = "MEAL_TYPE")
    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @Column(name = "MEAL_TIME")
    private LocalDateTime mealTime;

    @Column(name = "TOTAL_KCAL")
    private int totalKcal; // 계산된 식사 총 섭취 칼로리


    public static Meal newInstance(MealType mealType, LocalDateTime mealTime, List<MealFood> mealFoods) {
        Meal meal = new Meal();
        meal.mealType = mealType;
        meal.mealTime = mealTime;
        meal.addAllMealFoods(mealFoods);
        return meal;
    }


    /** HealthReport ↔ Meal 양방향 매핑 */
    public void setReport(HealthReport report) {
        this.report = report;
    }

    /** Meal ↔ MealFood 양방향 매핑 */
    public void addAllMealFoods(List<MealFood> mealFoods) {
        this.mealFoods.addAll(mealFoods);

        for (MealFood mealFood : mealFoods) {
            mealFood.setMeal(this);
        }
        this.setTotalKcal(); //
    }

    /** 해당 식사 총 칼로리 계산 */
    private void setTotalKcal() {
        this.totalKcal = this.mealFoods.stream()
                .mapToInt(mealFood -> mealFood.calculateMealFoodKcal())
                .sum();
    }
}
