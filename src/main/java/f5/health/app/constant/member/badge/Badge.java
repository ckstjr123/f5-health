package f5.health.app.constant.member;

import f5.health.app.constant.MappingEnum;

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

    @Override
    public String getLabel() {
        return this.label;
    }
}
