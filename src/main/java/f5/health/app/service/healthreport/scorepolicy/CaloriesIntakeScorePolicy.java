package f5.health.app.service.healthreport.scorepolicy;

import f5.health.app.service.healthreport.scorepolicy.vo.HealthSnapshot;

public class CaloriesIntakeScorePolicy implements ScorePolicy {

    @Override
    public int calculate(HealthSnapshot snapshot) {
        int totalKcal = snapshot.getNutritionContents().getTotalKcal();
        int recommendedCalories = snapshot.getRecommendedCalories();
        int diff = Math.abs(totalKcal - recommendedCalories);
        if (diff <= 100) return 10;  // 거의 일치
        if (diff <= 300) return 7;   // 양호한 범위
        if (diff <= 500) return 4;   // 주의 요망
        return 0; // 권장 섭취량과 큰 차이
    }
}
