package f5.health.app.entity.meal;

import f5.health.app.entity.Food;
import f5.health.app.service.healthreport.vo.request.MealFoodRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 식사 항목 엔티티 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEAL_FOOD")
public class MealFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_FOOD_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //양방향 연관 관계
    @JoinColumn(name = "MEAL_ID")
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOOD_CODE")
    private Food food;

    @Column(name = "COUNT")
    private double count; // 해당 음식 섭취 수량(0.5, 1 , 1.5...)

    /**
     * 식사한 음식
     */
    public static MealFood newInstance(EatenFoodMap eatenFoodMap, MealFoodRequest mealFoodRequest) {
        MealFood mealFood = new MealFood();
        mealFood.food = eatenFoodMap.get(mealFoodRequest.getFoodCode());
        mealFood.count = mealFoodRequest.getCount();
        return mealFood;
    }


    /**
     * MEAL ↔ MEAL_FOOD 양방향 연관관계 매핑
     */
    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    /**
     * 해당 식사 메뉴 수량에 따른 칼로리 계산
     */
    public int calculateMealFoodKcal() {
        return (int) (food.calculateServingKcal() * count);
    }

    // ========= 섭취한 음식 수량에 따른 탄수화물, 단백질, 지방 함량 ========= //
    public double calculateMealFoodCarbohydrate() {
        return (food.calculateServingCarbohydrate() * count);
    }
    public double calculateMealFoodProtein() {
        return (food.calculateServingProtein() * count);
    }
    public double calculateMealFoodFat() {
        return (food.calculateServingFat() * count);
    }

}
