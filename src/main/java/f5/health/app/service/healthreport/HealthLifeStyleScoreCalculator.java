package f5.health.app.service.healthreport;

import f5.health.app.service.healthreport.vo.request.NutritionFacts;
import f5.health.app.service.healthreport.vo.request.healthkit.HealthKit;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.Activity;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.SleepAnalysis;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.VitalSigns;
import org.springframework.stereotype.Component;

@Component
public class HealthLifeStyleScoreCalculator {

    public int calculateScore(HealthKit healthKit, NutritionFacts nutritionFacts) {
        Activity activity = healthKit.getActivity();
        VitalSigns vitalSigns = healthKit.getVitalSigns();
        SleepAnalysis sleepAnalysis = healthKit.getSleepAnalysis();
        int activeEnergyBurned = (int) activity.getActiveEnergyBurned();
        int totalKcal = nutritionFacts.getTotalKcal();

        return waterIntakeScore(healthKit.getWaterIntake())
               + smokingScore(healthKit.getSmokedCigarettes())
               + alcoholScore(healthKit.getConsumedAlcoholDrinks())
               + stepCountScore(activity.getStepCount())
               + workoutScore((int) activity.getAppleExerciseTime(), activeEnergyBurned)
               + heartRateScore(vitalSigns.getHeartRate())
               + totalCaloriesBurnedScore(activeEnergyBurned)
               + sleepScore(sleepAnalysis.getAsleepREM() + sleepAnalysis.getAsleepCore() + sleepAnalysis.getAsleepDeep())
               + caloriesIntakeScore(totalKcal)
               + pfcBalanceScore(totalKcal, nutritionFacts.getTotalCarbohydrate(), nutritionFacts.getTotalProtein(), nutritionFacts.getTotalFat());
    }

    private int waterIntakeScore(int waterIntake) {
        if (waterIntake >= 1500) return 10;
        if (waterIntake >= 1000) return 7;
        if (waterIntake >= 500) return 4;
        return 0;
    }

    private int smokingScore(int smokingAmount) {
        if (smokingAmount == 0) return 15;
        if (smokingAmount <= 5) return 10;
        if (smokingAmount <= 10) return 5;
        return 0;
    }

    private int alcoholScore(int alcoholAmount) {
        if (alcoholAmount == 0) return 10;
        if (alcoholAmount <= 2) return 7;
        if (alcoholAmount <= 4) return 4;
        return 0;
    }

    private int stepCountScore(int stepCount) {
        if (stepCount >= 10000) return 10;
        if (stepCount >= 7000) return 7;
        if (stepCount >= 4000) return 4;
        return 0;
    }

    private int workoutScore(int exerciseMinutes, int activeEnergyBurned) {
        if (exerciseMinutes >= 30 && activeEnergyBurned >= 300) return 15;
        if (exerciseMinutes >= 20 && activeEnergyBurned >= 200) return 10;
        if (exerciseMinutes >= 10 && activeEnergyBurned >= 100) return 5;
        return 0;
    }

    private int heartRateScore(int bpm) {
        if (bpm >= 60 && bpm <= 100) return 5;
        if (bpm >= 50 && bpm <= 110) return 2;
        return 0;
    }

    private int totalCaloriesBurnedScore(int activeEnergyBurned) {
        if (activeEnergyBurned >= 2000) return 5;
        if (activeEnergyBurned >= 1500) return 3;
        if (activeEnergyBurned >= 1000) return 1;
        return 0;
    }

    private int sleepScore(int sleepHours) {
        if (sleepHours >= 7 && sleepHours <= 9) return 10;
        if (sleepHours == 6) return 7;
        if (sleepHours <= 5) return 4;
        return 0;
    }

    private int caloriesIntakeScore(int totalKcal) {
        if (totalKcal >= 1800 && totalKcal <= 2200) return 5;
        if (totalKcal >= 1600 && totalKcal <= 2400) return 3;
        return 0;
    }

    private int pfcBalanceScore(int totalKcal, double totalCarbohydrate, double totalProtein, double totalFat) {
        if (totalKcal == 0) return 0;

        double carbRatio = (totalCarbohydrate * 4) / totalKcal;
        double proteinRatio = (totalProtein * 4) / totalKcal;
        double fatRatio = (totalFat * 9) / totalKcal;

        double carbDiff = Math.abs(carbRatio - 0.5);
        double proteinDiff = Math.abs(proteinRatio - 0.3);
        double fatDiff = Math.abs(fatRatio - 0.2);

        if (carbDiff <= 0.1 && proteinDiff <= 0.1 && fatDiff <= 0.1) return 15;
        if (carbDiff <= 0.2 && proteinDiff <= 0.2 && fatDiff <= 0.2) return 7;
        return 0;
    }
}
