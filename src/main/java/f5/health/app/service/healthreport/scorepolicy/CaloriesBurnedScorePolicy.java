package f5.health.app.service.healthreport.scorepolicy;

import f5.health.app.service.healthreport.scorepolicy.vo.HealthSnapshot;

public class CaloriesBurnedScorePolicy implements ScorePolicy {

    @Override
    public int calculate(HealthSnapshot snapshot) {
        double activeEnergyBurned = snapshot.getActivity().getActiveEnergyBurned();
        if (activeEnergyBurned >= 700) return 10;   // 매우 활발한 활동
        if (activeEnergyBurned >= 500) return 7;    // 활발한 활동
        if (activeEnergyBurned >= 300) return 5;    // 적당한 활동
        if (activeEnergyBurned >= 100) return 2;    // 가벼운 활동
        return 0; // 거의 활동 없음
    }
}
