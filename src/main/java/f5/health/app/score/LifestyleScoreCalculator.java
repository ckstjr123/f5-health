package f5.health.app.score;

public class LifestyleScoreCalculator {

        public int waterScore(int waterIntake) {
            if (waterIntake >= 1500) return 10;
            if (waterIntake >= 1000) return 7;
            if (waterIntake >= 500) return 4;
            return 0;
        }

        public int smokingScore(int smokingAmount) {
            if (smokingAmount == 0) return 15;
            if (smokingAmount <= 5) return 10;
            if (smokingAmount <= 10) return 5;
            return 0;
        }

        public int alcoholScore(int alcoholAmount) {
            if (alcoholAmount == 0) return 10;
            if (alcoholAmount <= 2) return 7;
            if (alcoholAmount <= 4) return 4;
            return 0;
        }

        public int stepScore(int stepCount) {
            if (stepCount >= 10000) return 10;
            if (stepCount >= 7000) return 7;
            if (stepCount >= 4000) return 4;
            return 0;
        }

    public int workoutScore(int minutes, int kcal) {
        if (minutes >= 30 && kcal >= 300) return 15;
        if (minutes >= 20 && kcal >= 200) return 10;
        if (minutes >= 10 && kcal >= 100) return 5;
        return 0;
    }


    public int heartRateScore(int bpm) {
            if (bpm >= 60 && bpm <= 100) return 5;
            if (bpm >= 50 && bpm <= 110) return 2;
            return 0;

        }

        public int totalCaloriesBurnedScore(int kcal) {
            if (kcal >= 2000) return 5;
            if (kcal >= 1500) return 3;
            if (kcal >= 1000) return 1;
            return 0;
        }

        public int sleepScore(int hours) {
            if (hours >= 7 && hours <= 9) return 10;
            if (hours == 6) return 7;
            if (hours <= 5) return 4;
            return 0;
        }

        public int intakeCaloriesScore(double kcal) {
            if (kcal >= 1800 && kcal <= 2200) return 5;
            if (kcal >= 1600 && kcal <= 2400) return 3;
            return 0;
        }

        public int pfcBalanceScore(double carbohydrate, double protein, double fat) {
            double totalKcal = carbohydrate * 4 + protein * 4 + fat * 9;
            if (totalKcal == 0) return 0;

            double carbRatio = (carbohydrate * 4) / totalKcal;
            double proteinRatio = (protein * 4) / totalKcal;
            double fatRatio = (fat * 9) / totalKcal;

            double carbDiff = Math.abs(carbRatio - 0.5);
            double proteinDiff = Math.abs(proteinRatio - 0.3);
            double fatDiff = Math.abs(fatRatio - 0.2);

            if (carbDiff <= 0.1 && proteinDiff <= 0.1 && fatDiff <= 0.1) return 15;
            if (carbDiff <= 0.2 && proteinDiff <= 0.2 && fatDiff <= 0.2) return 7;
            return 0;
        }
    }