package f5.health.app.service.healthreport;

import f5.health.app.service.healthreport.scorepolicy.*;

import java.util.Set;

public class ScorePolicyRegistry {
    protected static Set<ScorePolicy> getAllPoliciySet() {
        return Set.of(
                new WaterIntakeScorePolicy(),
                new SmokingScorePolicy(),
                new AlcoholScorePolicy(),
                new StepCountScorePolicy(),
                new WorkoutScorePolicy(),
                new HeartRateScorePolicy(),
                new CaloriesBurnedScorePolicy(),
                new SleepScorePolicy(),
                new CaloriesIntakeScorePolicy(),
                new PfcBalanceScorePolicy()
        );
    }
}
