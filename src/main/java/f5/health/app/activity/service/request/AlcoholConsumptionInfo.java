package f5.health.app.activity.service.request;

import f5.health.app.member.entity.AlcoholConsumption;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static f5.health.app.activity.constant.AlcoholType.ALCOHOL_TYPE_SIZE;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "음주 정보")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlcoholConsumptionInfo {

    @Valid
    @NotNull(message = "음주 정보를 입력해 주세요.")
    @Schema(description = "음주 목록", requiredMode = REQUIRED)
    @Size(max = ALCOHOL_TYPE_SIZE, message = "주류는 " + ALCOHOL_TYPE_SIZE + "개입니다.")
    private List<AlcoholConsumption> result;


    @Schema(hidden = true)
    public int getTotalAlcoholIntake() {
        return result.stream()
                .mapToInt(AlcoholConsumption::getAlcoholIntake)
                .sum();
    }
}
