package f5.health.app.service.healthreport;

import f5.health.app.service.healthreport.scorepolicy.ScorePolicy;
import f5.health.app.service.healthreport.scorepolicy.vo.HealthSnapshot;

import java.util.HashSet;
import java.util.Set;

public class HealthLifeStyleScoreCalculator {

    private final Set<ScorePolicy> scorePolicySet = new HashSet<>();

    protected int calculateScore(HealthSnapshot snapshot) {
        return scorePolicySet.stream()
                .mapToInt(scorePolicy -> scorePolicy.calculate(snapshot))
                .sum();
    }

    protected void addScorePolicy(Set<ScorePolicy> scorePolicySet) {
        this.scorePolicySet.addAll(scorePolicySet);
    }
}
