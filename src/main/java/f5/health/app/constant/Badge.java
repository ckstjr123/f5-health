package f5.health.app.constant;

public enum Badge {
    BEGINNER("비기너", 0L),
    ACTIVE("액티브", 2100L),
    PRO("프로", 12000),
    MASTER("마스터", 25000);

    private final String name;
    private final long cutOffScore;

    Badge(String name, long cutOffScore) {
        this.name = name;
        this.cutOffScore = cutOffScore;
    }
}
