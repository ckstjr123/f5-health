package f5.health.app.activity.service.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import f5.health.app.member.entity.AlcoholConsumption;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static f5.health.app.activity.constant.AlcoholType.ALCOHOL_TYPE_SIZE;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "음주 정보")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlcoholConsumptionInfo {

    @Valid
    @Schema(description = "음주 목록", requiredMode = REQUIRED)
    @Size(max = ALCOHOL_TYPE_SIZE, message = "주류는 " + ALCOHOL_TYPE_SIZE + "개입니다.")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<AlcoholConsumption> result = new ArrayList<>();


    @Schema(hidden = true)
    public int getTotalAlcoholIntake() {
        return result.stream()
                .mapToInt(AlcoholConsumption::getAlcoholIntake)
                .sum();
    }
}
