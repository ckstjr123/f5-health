package f5.health.app.constant.member;

public enum Gender {
    MALE("남자"), FEMALE("여자");

    private final String label;

    Gender(String label) {
        this.label = label;
    }
}
