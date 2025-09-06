package f5.health.app.meal.entity;

import f5.health.app.meal.constant.MealType;
import f5.health.app.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 식단 엔티티
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEAL", uniqueConstraints = {@UniqueConstraint(columnNames = {"MEMBER_ID", "EATEN_DATE", "MEAL_TYPE"})})
public class Meal {

    public static final int MENU_LIMIT_SIZE_PER_MEAL = 15; // 식사당 등록 가능한 메뉴 최대 개수

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "meal")
    private List<MealFood> mealFoods = new ArrayList<>();

    @Column(name = "MEAL_TYPE")
    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @Column(name = "EATEN_AT")
    private LocalDateTime eatenAt;

    @Column(name = "EATEN_DATE")
    private LocalDate eatenDate; // 날짜 조건 조회용

    @Column(name = "TOTAL_KCAL")
    private int totalKcal; // 계산된 식사 총 섭취 칼로리

    @Column(name = "TOTAL_CARBOHYDRATE")
    private double totalCarbohydrate;

    @Column(name = "TOTAL_PROTEIN")
    private double totalProtein;

    @Column(name = "TOTAL_FAT")
    private double totalFat;

    /**
     * 식단 생성 메서드
     */
    public static Meal newInstance(Member member, LocalDateTime eatenAt, MealType mealType, List<MealFood> mealFoods) {
        Meal meal = new Meal();
        meal.member = member;
        meal.eatenAt = eatenAt;
        meal.eatenDate = eatenAt.toLocalDate();
        meal.mealType = mealType;
        meal.addAllMealFoods(mealFoods);
        return meal;
    }

    /**
     * Meal ↔ MealFood 양방향 매핑
     */
    public void addAllMealFoods(List<MealFood> mealFoods) {
        mealFoods.forEach(mealFood -> {
            this.mealFoods.add(mealFood);
            mealFood.setMeal(this);
        });

        calculateNutritionFacts(); //
    }


    private void calculateNutritionFacts() {
        this.totalKcal = 0;
        this.totalCarbohydrate = 0.0;
        this.totalProtein = 0.0;
        this.totalFat = 0.0;
        mealFoods.forEach(mealFood -> {
            this.totalKcal += mealFood.calculateKcal();
            this.totalCarbohydrate += mealFood.calculateCarbohydrate();
            this.totalProtein += mealFood.calculateProtein();
            this.totalFat += mealFood.calculateFat();
        });
    }

}
