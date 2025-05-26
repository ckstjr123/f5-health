package f5.health.app.service.healthreport;

import f5.health.app.service.healthreport.scorepolicy.HealthScorePolicy;
import f5.health.app.service.healthreport.scorepolicy.vo.HealthSnapshot;
import io.jsonwebtoken.lang.Assert;

/** 건강 관련 요소에 대한 점수 측정기 */
public class HealthScoreCalculator {

    private HealthScorePolicy scorePolicy;

    protected HealthScoreCalculator(HealthScorePolicy scorePolicy) {
        Assert.notNull(scorePolicy);
        this.scorePolicy = scorePolicy;
    }

    protected int calculateScore(HealthSnapshot snapshot) {
        return scorePolicy.calculate(snapshot);
    }

    protected HealthScoreCalculator changeScorePolicy(HealthScorePolicy scorePolicy) {
        Assert.notNull(scorePolicy);
        this.scorePolicy = scorePolicy;
        return this;
    }
}
