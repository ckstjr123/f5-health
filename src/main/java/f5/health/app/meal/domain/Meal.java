package f5.health.app.meal.domain;

import f5.health.app.common.exception.AccessDeniedException;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.domain.embedded.Nutrients;
import f5.health.app.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static f5.health.app.meal.constant.MealErrorCode.NOT_FOUND_MEAL_OWNERSHIP;

/**
 * 식단 엔티티
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEAL", indexes = {@Index(columnList = "MEMBER_ID, EATEN_DATE, MEAL_TYPE")})
public class Meal {

    public static final int MENU_MIN_SIZE_PER_MEAL = 1;
    public static final int MENU_MAX_SIZE_PER_MEAL = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.REMOVE)
    private List<MealFood> mealFoods = new ArrayList<>();

    @Column(name = "MEAL_TYPE")
    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @Embedded
    private Nutrients nutrients;

    @Column(name = "EATEN_AT")
    private LocalDateTime eatenAt;

    @Column(name = "EATEN_DATE")
    private LocalDate eatenDate; // 날짜 조건 조회용


    public static Meal createMeal(Member member, LocalDateTime eatenAt, MealType mealType, List<MealFood> mealFoods) {
        Meal meal = new Meal();
        meal.member = member;
        meal.eatenAt = eatenAt;
        meal.eatenDate = eatenAt.toLocalDate();
        meal.mealType = mealType;
        meal.addAllMealFoods(mealFoods);
        meal.calculateNutrients(); //
        return meal;
    }

    /**
     * Meal ↔ MealFood
     */
    public void addAllMealFoods(List<MealFood> mealFoods) {
        mealFoods.forEach(mealFood -> {
            this.mealFoods.add(mealFood);
            mealFood.setMeal(this);
        });
    }


    public void calculateNutrients() {
        this.nutrients = Nutrients.from(mealFoods);
    }

    private boolean isOwnedBy(Long memberId) {
        return member.getId().equals(memberId);
    }

    public void validateOwnership(Long memberId) {
        if (!this.isOwnedBy(memberId)) {
            throw new AccessDeniedException(NOT_FOUND_MEAL_OWNERSHIP);
        }
    }

    public boolean hasSameEatenDate(LocalDate eatenDate) {
        return this.eatenDate.isEqual(eatenDate);
    }

    public boolean isDifferentTypeFrom(MealType mealType) {
        return this.mealType != mealType;
    }

    public void updateMealTime(LocalDateTime eatenAt, MealType mealType) {
        this.eatenAt = eatenAt;
        this.eatenDate = eatenAt.toLocalDate();
        this.mealType = mealType;
    }

}
