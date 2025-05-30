package f5.health.app.constant;

import java.util.Arrays;

/** 주류 */
public enum AlcoholType implements MappingEnum {

    SOJU("소주", 4.3),
    BEER("맥주", 5.0);

    public static final int ALCOHOL_TYPE_SIZE = 2;
    public static final double AVERAGE_MAJOR_ALCOHOL_PRICE_PER_ML = Arrays.stream(values())
            .mapToDouble(AlcoholType::pricePerML)
            .average()
            .orElseThrow();
    private final String label;
    private final double pricePerML;

    AlcoholType(String label, double pricePerML) {
        this.label = label;
        this.pricePerML = pricePerML;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public double pricePerML() {
        return this.pricePerML;
    }
}
