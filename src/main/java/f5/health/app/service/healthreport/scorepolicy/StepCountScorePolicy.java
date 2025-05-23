package f5.health.app.service.healthreport.scorepolicy;

import f5.health.app.service.healthreport.scorepolicy.vo.HealthSnapshot;

public class StepCountScorePolicy implements ScorePolicy {

    @Override
    public int calculate(HealthSnapshot snapshot) {
        int stepCount = snapshot.getActivity().getStepCount();
        if (stepCount >= 12000) return 10;
        if (stepCount >= 10000) return 9;
        if (stepCount >= 8000) return 7;
        if (stepCount >= 6000) return 5;
        if (stepCount >= 4000) return 3;
        if (stepCount >= 2000) return 1;
        return 0;
    }
}
