package f5.health.app.activity.service.request;

import f5.health.app.member.entity.AlcoholConsumption;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static f5.health.app.activity.constant.AlcoholType.ALCOHOL_TYPE_SIZE;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "음주 정보")
public record AlcoholConsumptionInfo(
        @Schema(description = "음주 목록", requiredMode = REQUIRED) @Valid @Size(max = ALCOHOL_TYPE_SIZE, message = "주류는 " + ALCOHOL_TYPE_SIZE + "개입니다.") List<AlcoholConsumption> alcoholResult) {

    public AlcoholConsumptionInfo {
        alcoholResult = Objects.requireNonNullElseGet(alcoholResult, ArrayList::new);
    }

    @Schema(hidden = true)
    public int getTotalAlcoholIntake() {
        return alcoholResult.stream()
                .mapToInt(AlcoholConsumption::alcoholIntake)
                .sum();
    }
}
