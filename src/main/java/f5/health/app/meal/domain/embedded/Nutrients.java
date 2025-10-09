package f5.health.app.meal.domain.embedded;

import f5.health.app.meal.domain.MealFood;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nutrients {

    @Column(name = "TOTAL_KCAL")
    private int kcal;

    @Column(name = "TOTAL_CARBOHYDRATE")
    private double carbohydrate;

    @Column(name = "TOTAL_PROTEIN")
    private double protein;

    @Column(name = "TOTAL_FAT")
    private double fat;

    private Nutrients(int kcal, double carbohydrate, double protein, double fat) {
        this.kcal = kcal;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
    }

    public static Nutrients from(List<MealFood> mealFoods) {
        int totalKcal = 0;
        double totalCarbohydrate = 0, totalProtein = 0, totalFat = 0;
        for (MealFood mealFood : mealFoods) {
            totalKcal += mealFood.calculateKcal();
            totalCarbohydrate += mealFood.calculateCarbohydrate();
            totalProtein += mealFood.calculateProtein();
            totalFat += mealFood.calculateFat();
        }

        return new Nutrients(totalKcal, totalCarbohydrate, totalProtein, totalFat);
    }
}
