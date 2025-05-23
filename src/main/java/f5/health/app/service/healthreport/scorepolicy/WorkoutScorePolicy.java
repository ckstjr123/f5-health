package f5.health.app.service.healthreport.scorepolicy;

import f5.health.app.service.healthreport.scorepolicy.vo.HealthSnapshot;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.Activity;

public class WorkoutScorePolicy implements ScorePolicy {

    @Override
    public int calculate(HealthSnapshot snapshot) {
        Activity activity = snapshot.getActivity();
        double exerciseMinutes = activity.getAppleExerciseTime();
        double activeEnergyBurned = activity.getActiveEnergyBurned();
        if (exerciseMinutes >= 45 && activeEnergyBurned >= 400) return 15;
        if (exerciseMinutes >= 30 && activeEnergyBurned >= 300) return 12;
        if (exerciseMinutes >= 20 && activeEnergyBurned >= 200) return 9;
        if (exerciseMinutes >= 15 && activeEnergyBurned >= 150) return 6;
        if (exerciseMinutes >= 10 && activeEnergyBurned >= 100) return 3;
        return 0;
    }
}
