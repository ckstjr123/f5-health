package f5.health.app.constant.meal;

import f5.health.app.constant.MappingEnum;

public enum MealType implements MappingEnum {

    BREAKFAST("아침"),
    LUNCH("점심"),
    DINNER("저녁"),
    DESSERT("간식");

    private final String label;

    MealType(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return this.label;
    }
}
