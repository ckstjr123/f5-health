package f5.health.app.service.healthreport;

import f5.health.app.constant.member.Gender;
import f5.health.app.entity.Member;
import f5.health.app.service.healthreport.vo.MealsNutritionContents;
import f5.health.app.service.healthreport.vo.request.healthkit.HealthKit;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.Activity;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.SleepAnalysis;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.VitalSigns;

import static f5.health.app.constant.member.Gender.FEMALE;
import static f5.health.app.constant.member.Gender.MALE;

public class HealthLifeStyleScoreCalculator {

    public int calculateScore(Member member, HealthKit healthKit, MealsNutritionContents nutritionContents) {
        Activity activity = healthKit.getActivity();
        VitalSigns vitalSigns = healthKit.getVitalSigns();
        SleepAnalysis sleepAnalysis = healthKit.getSleepAnalysis();
        int activeEnergyBurned = (int) activity.getActiveEnergyBurned();
        int totalKcal = nutritionContents.getTotalKcal();

        return waterIntakeScore(member.getWeight(), member.getGender(), healthKit.getWaterIntake())
                + smokingScore(member.getDaySmokeCigarettes(), healthKit.getSmokedCigarettes())
                + alcoholScore(member.getWeekAlcoholDrinks(), healthKit.getConsumedAlcoholDrinks())
                + stepCountScore(activity.getStepCount())
                + workoutScore((int) activity.getAppleExerciseTime(), activeEnergyBurned)
                + heartRateScore(vitalSigns.getHeartRate())
                + totalCaloriesBurnedScore(activeEnergyBurned)
                + sleepScore(sleepAnalysis.getAsleepREM() + sleepAnalysis.getAsleepCore() + sleepAnalysis.getAsleepDeep())
                + caloriesIntakeScore(totalKcal, member.getRecommendedCalories())
                + pfcBalanceScore(totalKcal, nutritionContents.getTotalCarbohydrate(), nutritionContents.getTotalProtein(), nutritionContents.getTotalFat());
    }

    private int waterIntakeScore(int weight, Gender gender, int waterIntake) {
        double recommendedIntake = 0.0;
        // 성별에 따른 수분 권장 섭취량 계산 (ml 단위)
        if (gender == MALE) {
            recommendedIntake = weight * 35.0;  // 남성: 35ml/kg
        } else if (gender == FEMALE) {
            recommendedIntake = weight * 31.0;  // 여성: 31ml/kg
        }

        double ratio = waterIntake / recommendedIntake;

        if (ratio >= 1.0) return 10; // 권장량 이상
        if (ratio >= 0.8) return 8;
        if (ratio >= 0.6) return 5;
        if (ratio >= 0.4) return 2;
        return 0;
    }

    private int smokingScore(int daySmokeCigarettes, int smokedCigarettes) {
        int baseline = daySmokeCigarettes;
        // 비흡연자라면 그대로 가중치 적용
        if (baseline == 0) {
            return smokedCigarettes == 0 ? 15 : 0;
        }

        int diff = baseline - smokedCigarettes;

        if (diff >= baseline) return 15; // 완전 금연
        if (diff >= baseline * 0.75) return 12;
        if (diff >= baseline * 0.5) return 9;
        if (diff >= baseline * 0.25) return 6;
        if (diff > 0) return 3;

        return 0; // 늘었거나 그대로인 경우
    }

    private int alcoholScore(int weekAlcoholDrinks, int consumedAlcoholDrinks) {
        int baseline = weekAlcoholDrinks;
        int avgPerDay = baseline / 7;

        // 비음주자의 경우
        if (baseline == 0) {
            return consumedAlcoholDrinks == 0 ? 10 : 0;
        }

        int diff = avgPerDay - consumedAlcoholDrinks;

        if (diff >= avgPerDay) return 10; // 완전 금주
        if (diff >= avgPerDay * 0.75) return 8;
        if (diff >= avgPerDay * 0.5) return 6;
        if (diff >= avgPerDay * 0.25) return 4;
        if (diff > 0) return 2;

        return 0; // 음주량 유지 or 증가
    }


    private int stepCountScore(int stepCount) {
        if (stepCount >= 12000) return 10;
        if (stepCount >= 10000) return 9;
        if (stepCount >= 8000) return 7;
        if (stepCount >= 6000) return 5;
        if (stepCount >= 4000) return 3;
        if (stepCount >= 2000) return 1;
        return 0;
    }


    private int workoutScore(int exerciseMinutes, int activeEnergyBurned) {
        if (exerciseMinutes >= 45 && activeEnergyBurned >= 400) return 15;
        if (exerciseMinutes >= 30 && activeEnergyBurned >= 300) return 12;
        if (exerciseMinutes >= 20 && activeEnergyBurned >= 200) return 9;
        if (exerciseMinutes >= 15 && activeEnergyBurned >= 150) return 6;
        if (exerciseMinutes >= 10 && activeEnergyBurned >= 100) return 3;
        return 0;
    }


    private int heartRateScore(int bpm) {
        if (bpm >= 60 && bpm <= 80) return 10;   // 안정적인 이상적인 범위
        if ((bpm > 80 && bpm <= 90) || (bpm >= 50 && bpm < 60)) return 6; // 살짝 벗어난 정상 범위
        if ((bpm > 90 && bpm <= 100) || (bpm >= 45 && bpm < 50)) return 3; // 다소 높은/낮은 경우
        return 0; // 위험 수준이거나 측정 오류 가능성
    }


    private int totalCaloriesBurnedScore(int activeEnergyBurned) {
        if (activeEnergyBurned >= 700) return 10;   // 매우 활발한 활동
        if (activeEnergyBurned >= 500) return 7;    // 활발한 활동
        if (activeEnergyBurned >= 300) return 5;    // 적당한 활동
        if (activeEnergyBurned >= 100) return 2;    // 가벼운 활동
        return 0; // 거의 활동 없음
    }


    private int sleepScore(int sleepMinutes) {
        int sleepHours = sleepMinutes / 60;

        if (sleepHours >= 7 && sleepHours <= 9) return 10;   // 이상적 수면
        if (sleepHours == 6 || sleepHours == 10) return 7;   // 조금 짧거나 길지만 양호
        if (sleepHours == 5 || sleepHours == 11) return 4;   // 불규칙 수면
        return 0; // 수면 부족 or 과도
    }


    private int caloriesIntakeScore(int totalKcal, int recommendedCalories) {
        int diff = Math.abs(totalKcal - recommendedCalories);

        if (diff <= 100) return 10;  // 거의 일치
        if (diff <= 300) return 7;   // 양호한 범위
        if (diff <= 500) return 4;   // 주의 요망
        return 0; // 권장 섭취량과 큰 차이
    }


    private int pfcBalanceScore(int totalKcal, double totalCarbohydrate, double totalProtein, double totalFat) {
        if (totalKcal == 0) return 0;

        double carbRatio = (totalCarbohydrate * 4) / totalKcal;
        double proteinRatio = (totalProtein * 4) / totalKcal;
        double fatRatio = (totalFat * 9) / totalKcal;

        // 이상적인 비율: 탄수화물 50%, 단백질 30%, 지방 20%
        double carbDiff = Math.abs(carbRatio - 0.5);
        double proteinDiff = Math.abs(proteinRatio - 0.3);
        double fatDiff = Math.abs(fatRatio - 0.2);

        double totalDiff = carbDiff + proteinDiff + fatDiff;

        if (totalDiff <= 0.15) return 15;  // 이상적인 비율
        if (totalDiff <= 0.30) return 10;  // 좋은 균형
        if (totalDiff <= 0.50) return 5;   // 약간 불균형
        return 0; // 불균형
    }

}
