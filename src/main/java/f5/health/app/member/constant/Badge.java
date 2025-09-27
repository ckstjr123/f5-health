package f5.health.app.member.constant;

import f5.health.app.common.MappingEnum;

import java.util.Arrays;
import java.util.Comparator;

public enum Badge implements MappingEnum {

    BEGINNER("비기너", 0L),
    ACTIVE("액티브", 3000L),
    PRO("프로", 25000),
    MASTER("마스터", 62000);

    private final String label;
    private final long cutOffPoint;

    Badge(String label, long cutOffPoint) {
        this.label = label;
        this.cutOffPoint = cutOffPoint;
    }

    public String label() {
        return this.label;
    }

    public long cutOffPoint() {
        return this.cutOffPoint;
    }

    /** totalPoint를 가장 높은 배지부터 낮은 순으로 cutOffPoint와 비교하여 해당되는 배지를 찾는 즉시 반환 */
    public static Badge fromTotalPoint(Long totalPoint) {
        return Arrays.stream(Badge.values())
                .sorted(Comparator.comparingLong(Badge::cutOffPoint).reversed()) // cutOffPoint 기준으로 배지 역순 정렬
                .filter(badge -> totalPoint >= badge.cutOffPoint)
                .findFirst()
                .orElseThrow();
    }
}
