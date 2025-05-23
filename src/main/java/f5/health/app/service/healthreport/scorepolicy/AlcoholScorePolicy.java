package f5.health.app.service.healthreport.scorepolicy;

import f5.health.app.service.healthreport.scorepolicy.vo.HealthSnapshot;

public class AlcoholScorePolicy implements ScorePolicy {

    @Override
    public int calculate(HealthSnapshot snapshot) {
        int baseline = snapshot.getWeekAlcoholDrinks();
        int consumedAlcoholDrinks = snapshot.getConsumedAlcoholDrinks();
        // 비음주자의 경우
        if (baseline == 0) {
            return consumedAlcoholDrinks == 0 ? 10 : 0;
        }

        int avgPerDay = baseline / 7;
        int diff = avgPerDay - consumedAlcoholDrinks;

        if (diff >= avgPerDay) return 10; // 완전 금주
        if (diff >= avgPerDay * 0.75) return 8;
        if (diff >= avgPerDay * 0.5) return 6;
        if (diff >= avgPerDay * 0.25) return 4;
        if (diff > 0) return 2;

        return 0; // 음주량 유지 or 증가
    }
}
