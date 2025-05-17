package f5.health.app.constant.member.badge;

import f5.health.app.constant.MappingEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;

@Getter
public enum Badge implements MappingEnum {

    BEGINNER("비기너", 0L),
    ACTIVE("액티브", 2100L),
    PRO("프로", 25000),
    MASTER("마스터", 55000);

    private final String label;
    private final long cutOffScore;

    Badge(String label, long cutOffScore) {
        this.label = label;
        this.cutOffScore = cutOffScore;
    }


    /** totalScore를 가장 높은 배지부터 낮은 순으로 cutOffScore와 비교하여 해당되는 배지를 찾는 즉시 반환 */
    public static Badge fromTotalScore(Long totalScore) {
        return Arrays.stream(Badge.values())
                .sorted(Comparator.comparingLong(Badge::getCutOffScore).reversed()) // cutOffScore 기준으로 배지 역순 정렬
                .filter(badge -> totalScore >= badge.cutOffScore)
                .findFirst()
                .orElseThrow();
    }
}
