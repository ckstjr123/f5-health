package f5.health.app.constant;

public enum MealType {

    BREAKFAST("아침"),
    LUNCH("점심"),
    DINNER("저녁"),
    DESSERT("간식");

    private final String label;

    MealType(String label) {
        this.label = label;
    }
}
