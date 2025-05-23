package f5.health.app.service.healthreport.scorepolicy;

import f5.health.app.service.healthreport.scorepolicy.vo.HealthSnapshot;
import f5.health.app.service.healthreport.vo.MealsNutritionContents;

public class PfcBalanceScorePolicy implements ScorePolicy {

    @Override
    public int calculate(HealthSnapshot snapshot) {
        MealsNutritionContents nutritionContents = snapshot.getNutritionContents();
        int totalKcal = nutritionContents.getTotalKcal();
        if (totalKcal == 0) {
            return 0;
        }

        double totalDiff = getTotalDiff(nutritionContents, totalKcal);
        if (totalDiff <= 0.15) return 15;  // 이상적인 비율
        if (totalDiff <= 0.30) return 10;  // 좋은 균형
        if (totalDiff <= 0.50) return 5;   // 약간 불균형
        return 0; // 불균형
    }

    private double getTotalDiff(MealsNutritionContents nutritionContents, int totalKcal) {
        double carbRatio = (nutritionContents.getTotalCarbohydrate() * 4) / totalKcal;
        double proteinRatio = (nutritionContents.getTotalProtein() * 4) / totalKcal;
        double fatRatio = (nutritionContents.getTotalFat() * 9) / totalKcal;

        // 이상적인 비율: 탄수화물 50%, 단백질 30%, 지방 20%
        double carbDiff = Math.abs(carbRatio - 0.5);
        double proteinDiff = Math.abs(proteinRatio - 0.3);
        double fatDiff = Math.abs(fatRatio - 0.2);

        return (carbDiff + proteinDiff + fatDiff);
    }
}
