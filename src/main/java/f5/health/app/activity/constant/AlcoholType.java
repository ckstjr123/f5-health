package f5.health.app.activity.constant;

import f5.health.app.common.MappingEnum;

public enum AlcoholType implements MappingEnum {

    SOJU("소주"),
    BEER("맥주"),
    MAKGEOLLI("막걸리"),
    WHISKY("위스키"),
    WINE("와인");

    public static final int ALCOHOL_TYPE_SIZE = 2;
    private final String label;

    AlcoholType(String label) {
        this.label = label;
    }

    public static int size() {
        return AlcoholType.values().length;
    }

    @Override
    public String label() {
        return this.label;
    }
}
