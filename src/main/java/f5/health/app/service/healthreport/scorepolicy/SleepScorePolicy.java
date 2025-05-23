package f5.health.app.service.healthreport.scorepolicy;

import f5.health.app.service.healthreport.scorepolicy.vo.HealthSnapshot;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.SleepAnalysis;

public class SleepScorePolicy implements ScorePolicy {

    @Override
    public int calculate(HealthSnapshot snapshot) {
        SleepAnalysis sleepAnalysis = snapshot.getSleepAnalysis();
        int sleepMinutes = sleepAnalysis.getAsleepREM() + sleepAnalysis.getAsleepCore() + sleepAnalysis.getAsleepDeep();
        int sleepHours = sleepMinutes / 60;
        if (sleepHours >= 7 && sleepHours <= 9) return 10;   // 이상적 수면
        if (sleepHours == 6 || sleepHours == 10) return 7;   // 조금 짧거나 길지만 양호
        if (sleepHours == 5 || sleepHours == 11) return 4;   // 불규칙 수면
        return 0; // 수면 부족 or 과도
    }
}
