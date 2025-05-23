package f5.health.app.entity.meal;

import f5.health.app.constant.meal.MealType;
import f5.health.app.entity.healthreport.HealthReport;
import f5.health.app.service.healthreport.vo.request.MealFoodRequest;
import f5.health.app.service.healthreport.vo.request.MealRequest;
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

    @Column(name = "TOTAL_CARBOHYDRATE")
    private double totalCarbohydrate;

    @Column(name = "TOTAL_PROTEIN")
    private double totalProtein;

    @Column(name = "TOTAL_FAT")
    private double totalFat;

    /** 식단 생성 메서드 */
    public static Meal newInstance(final EatenFoodMap eatenFoodMap, MealRequest mealRequest) {
        Meal meal = new Meal();
        meal.mealType = mealRequest.getMealType();
        meal.mealTime = mealRequest.getMealTime();
        meal.addAllMealFoods(createMealFoods(eatenFoodMap, mealRequest.getMealFoodRequestList()));
        return meal;
    }

    /** 식사당 먹은 음식 및 각 수량을 나타내는 MealFoods */
    private static List<MealFood> createMealFoods(final EatenFoodMap eatenFoodMap, List<MealFoodRequest> mealFoodRequestList) {
        return mealFoodRequestList.stream()
                .map(mealFoodRequest -> {
                    return MealFood.newInstance(eatenFoodMap, mealFoodRequest);
                })
                .toList();
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
        this.setNutritionContents(); //
    }

    /** 식사 총 섭취 칼로리 및 3대 영양소 계산해서 저장 */
    private void setNutritionContents() {
        this.totalKcal = 0;
        this.totalCarbohydrate = 0.0; this.totalProtein = 0.0; this.totalFat = 0.0;
        for (MealFood mealFood : this.mealFoods) {
            this.totalKcal += mealFood.calculateKcal();
            this.totalCarbohydrate += mealFood.calculateCarbohydrate();
            this.totalProtein += mealFood.calculateProtein();
            this.totalFat += mealFood.calculateFat();
        }
    }

/*
    private void setTotalKcal() {
        this.totalKcal = this.mealFoods.stream()
                .mapToInt(MealFood::calculateKcal)
                .sum();
    }

    public double getTotalCarbohydrate() {
        return this.mealFoods.stream()
                .mapToDouble(MealFood::calculateCarbohydrate)
                .sum();
    }
    public double getTotalProtein() {
        return this.mealFoods.stream()
                .mapToDouble(MealFood::calculateProtein)
                .sum();
    }
    public double getTotalFat() {
        return this.mealFoods.stream()
                .mapToDouble(MealFood::calculateFat)
                .sum();
    }*/

}
