package f5.health.app.service.healthreport.scorepolicy;

import f5.health.app.service.healthreport.scorepolicy.vo.HealthSnapshot;

/** 건강 습관 점수 항목별 채점 방식 */
public interface ScorePolicy {
    int calculate(HealthSnapshot snapshot);
}
