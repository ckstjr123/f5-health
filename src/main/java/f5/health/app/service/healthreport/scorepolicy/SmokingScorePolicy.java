package f5.health.app.service.healthreport.scorepolicy;

import f5.health.app.service.healthreport.scorepolicy.vo.HealthSnapshot;

public class SmokingScorePolicy implements ScorePolicy {

    @Override
    public int calculate(HealthSnapshot snapshot) {
        int baseline = snapshot.getDaySmokeCigarettes();
        int smokedCigarettes = snapshot.getSmokedCigarettes();
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
}
