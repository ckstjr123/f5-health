package f5.health.app.service.healthreport.scorepolicy.vo;

import f5.health.app.constant.member.Gender;
import f5.health.app.entity.Member;
import f5.health.app.service.healthreport.vo.MealsNutritionContents;
import f5.health.app.service.healthreport.vo.request.healthkit.HealthKit;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.Activity;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.SleepAnalysis;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.VitalSigns;
import lombok.Getter;

@Getter
public class HealthSnapshot {

    private final Gender gender;
    private final int weight;
    private final int waterIntake;
    private final int daySmokeCigarettes, smokedCigarettes;
    private final int weekAlcoholDrinks, consumedAlcoholDrinks;
    private final int recommendedCalories;

    private final Activity activity;
    private final VitalSigns vitalSigns;
    private final SleepAnalysis sleepAnalysis;

    private final MealsNutritionContents nutritionContents;

    private HealthSnapshot(Member member, HealthKit healthKit, MealsNutritionContents nutritionContents) {
        this.gender = member.getGender();
        this.weight = member.getWeight();
        this.waterIntake = healthKit.getWaterIntake();
        this.daySmokeCigarettes = member.getDaySmokeCigarettes();
        this.smokedCigarettes = healthKit.getSmokedCigarettes();
        this.weekAlcoholDrinks = member.getWeekAlcoholDrinks();
        this.consumedAlcoholDrinks = healthKit.getConsumedAlcoholDrinks();
        this.recommendedCalories = member.getRecommendedCalories();

        this.activity = healthKit.getActivity();
        this.vitalSigns = healthKit.getVitalSigns();
        this.sleepAnalysis = healthKit.getSleepAnalysis();

        this.nutritionContents = nutritionContents;
    }

    public static HealthSnapshot of(Member member, HealthKit healthKit, MealsNutritionContents nutritionContents) {
        return new HealthSnapshot(member, healthKit, nutritionContents);
    }
}
