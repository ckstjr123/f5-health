package f5.health.app.service.healthreport.scorepolicy;

import f5.health.app.service.healthreport.scorepolicy.vo.HealthSnapshot;

public class HeartRateScorePolicy implements ScorePolicy {

    @Override
    public int calculate(HealthSnapshot snapshot) {
        int bpm = snapshot.getVitalSigns().getHeartRate();
        if (bpm >= 60 && bpm <= 80) return 10;   // 안정적인 이상적인 범위
        if ((bpm > 80 && bpm <= 90) || (bpm >= 50 && bpm < 60)) return 6; // 살짝 벗어난 정상 범위
        if ((bpm > 90 && bpm <= 100) || (bpm >= 45 && bpm < 50)) return 3; // 다소 높은/낮은 경우
        return 0; // 위험 수준이거나 측정 오류 가능성
    }
}
