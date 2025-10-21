package f5.health.app.meal.constant;

import f5.health.app.common.MappingEnum;

public enum MealType implements MappingEnum {

    BREAKFAST("아침", 1),
    LUNCH("점심", 1),
    DINNER("저녁", 1),
    SNACK("간식", 3);

    private final String label;
    private final int maxCountPerDay;

    MealType(String label, int maxCountPerDay) {
        this.label = label;
        this.maxCountPerDay = maxCountPerDay;
    }

    @Override
    public String label() {
        return this.label;
    }

    public int maxCountPerDay() {
        return this.maxCountPerDay;
    }
}
