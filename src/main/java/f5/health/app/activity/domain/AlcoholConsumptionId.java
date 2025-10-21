package f5.health.app.activity.domain.alcoholconsumption;

import f5.health.app.activity.constant.AlcoholType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlcoholConsumptionId implements Serializable {
    private Long activity; // Activity activity;
    private AlcoholType alcoholType;

    private AlcoholConsumptionId(Long activityId, AlcoholType alcoholType) {
        this.activity = activityId;
        this.alcoholType = alcoholType;
    }

    public static AlcoholConsumptionId of(Long activityId, AlcoholType alcoholType) {
        return new AlcoholConsumptionId(activityId, alcoholType);
    }
}

