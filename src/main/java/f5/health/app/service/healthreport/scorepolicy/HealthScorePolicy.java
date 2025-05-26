package f5.health.app.service.healthreport.scorepolicy;

import f5.health.app.service.healthreport.scorepolicy.vo.HealthSnapshot;

/** 건강 관련 요소별 점수 측정 방식 */
public interface HealthScorePolicy {
    int calculate(HealthSnapshot snapshot);
}
