package f5.health.app.activity.domain.alcoholconsumption;

import f5.health.app.activity.constant.AlcoholType;
import f5.health.app.activity.vo.ActivityRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlcoholConsumptionFactory {

    public static AlcoholConsumption of(AlcoholType alcoholType, int intake) {
        return new AlcoholConsumption(alcoholType, intake);
    }

    public static List<AlcoholConsumption> from(List<ActivityRequest.AlcoholConsumptionParam> alcoholParams) {
        Map<AlcoholType, List<ActivityRequest.AlcoholConsumptionParam>> paramsByAlcoholType = alcoholParams.stream()
                .collect(Collectors.groupingBy(ActivityRequest.AlcoholConsumptionParam::alcoholType));

        return paramsByAlcoholType.entrySet().stream()
                .map(entry -> {
                    AlcoholType alcoholType = entry.getKey();
                    List<ActivityRequest.AlcoholConsumptionParam> params = entry.getValue();
                    int intake = params.stream()
                            .mapToInt(ActivityRequest.AlcoholConsumptionParam::intake)
                            .sum();
                    return new AlcoholConsumption(alcoholType, intake);
                })
                .toList();
    }

}
