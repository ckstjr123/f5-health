package f5.health.app.activity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

import static f5.health.app.activity.domain.Activity.DAILY_MAX_CIGARETTES;
import static f5.health.app.activity.domain.Activity.DAILY_MAX_WATER_ML;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "활동 기록 업데이트", requiredMode = REQUIRED)
public class ActivityUpdateRequest {

    @Schema(description = "음수량(ml)", example = "650", nullable = true)
    @Positive
    @Max(value = DAILY_MAX_WATER_ML, message = "하루에 기록 가능한 최대 음수량은 " + (DAILY_MAX_WATER_ML / 1000) + "리터입니다.")
    private final Integer waterIntake;

    @Schema(description = "흡연량(개비)", example = "6", nullable = true)
    @PositiveOrZero
    @Max(value = DAILY_MAX_CIGARETTES, message = "하루에 기록 가능한 흡연량 최대치는 " + DAILY_MAX_CIGARETTES + "개비입니다.")
    private final Integer smokedCigarettes;

    public ActivityUpdateRequest(Integer waterIntake, Integer smokedCigarettes) {
        this.waterIntake = waterIntake;
        this.smokedCigarettes = smokedCigarettes;
    }
}
