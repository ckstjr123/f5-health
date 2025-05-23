package f5.health.app.service.healthreport.scorepolicy;

import f5.health.app.constant.member.Gender;
import f5.health.app.service.healthreport.scorepolicy.vo.HealthSnapshot;

import static f5.health.app.constant.member.Gender.FEMALE;
import static f5.health.app.constant.member.Gender.MALE;

public class WaterIntakeScorePolicy implements ScorePolicy {

    @Override
    public int calculate(HealthSnapshot snapshot) {
        Gender gender = snapshot.getGender();
        int weight = snapshot.getWeight();
        // 성별에 따른 수분 권장 섭취량 계산 (ml 단위)
        double recommendedIntake = 0.0;
        if (gender == MALE) {
            recommendedIntake = weight * 35.0;  // 남성: 35ml/kg
        } else if (gender == FEMALE) {
            recommendedIntake = weight * 31.0;  // 여성: 31ml/kg
        }

        double ratio = snapshot.getWaterIntake() / recommendedIntake;

        if (ratio >= 1.0) return 10; // 권장량 이상
        if (ratio >= 0.8) return 8;
        if (ratio >= 0.6) return 5;
        if (ratio >= 0.4) return 2;
        return 0;
    }
}
