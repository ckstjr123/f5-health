package f5.health.app.meal.domain;

import f5.health.app.food.entity.Food;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

/** 식사 음식 엔티티 */
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
    @JoinColumn(name = "FOOD_ID")
    private Food food;

    @Column(name = "COUNT")
    private double count; // 해당 음식 섭취 수량(0.5, 1 , 1.5...)

    public static MealFood of(Food food, double count) {
        MealFood mealFood = new MealFood();
        mealFood.food = food;
        mealFood.count = count;
        return mealFood;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }


    public void update(Food food, double count) {
        this.food = food;
        this.count = count;
    }

    public int calculateKcal() {
        return (int) Math.round(food.getKcal() * count);
    }

    public double calculateCarbohydrate() {
        return (food.getCarbohydrate() * count);
    }

    public double calculateProtein() {
        return (food.getProtein() * count);
    }

    public double calculateFat() {
        return (food.getFat() * count);
    }

}
