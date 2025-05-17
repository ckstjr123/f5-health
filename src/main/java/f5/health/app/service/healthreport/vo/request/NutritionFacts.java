package f5.health.app.service.healthreport.vo.request;

import f5.health.app.entity.meal.Meal;
import lombok.Getter;

import java.util.List;

@Getter
public class NutritionFacts {

    private final int totalKcal;
    private final double totalCarbohydrate, totalProtein, totalFat;

    private NutritionFacts(int totalKcal, double totalCarbohydrate, double totalProtein, double totalFat) {
        this.totalKcal = totalKcal;
        this.totalCarbohydrate = totalCarbohydrate;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
    }

    public static NutritionFacts from(List<Meal> meals) {
        int totalKcal = 0;
        double totalCarbohydrate = 0.0 , totalProtein = 0.0 , totalFat = 0.0;
        for (Meal meal : meals) {
            totalKcal += meal.getTotalKcal();
            totalCarbohydrate += meal.getTotalCarbohydrate();
            totalProtein += meal.getTotalProtein();
            totalFat += meal.getTotalFat();
        }
        return new NutritionFacts(totalKcal, totalCarbohydrate, totalProtein, totalFat);
    }
}
