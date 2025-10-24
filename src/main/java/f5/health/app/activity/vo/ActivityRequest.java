package f5.health.app.activity.vo;

import f5.health.app.activity.domain.AlcoholType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

import static f5.health.app.activity.domain.AlcoholType.ALCOHOL_TYPE_SIZE;
import static f5.health.app.activity.domain.Activity.*;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "활동 데이터 기록 요청 VO", requiredMode = REQUIRED)
public final class ActivityRequest {

    @Schema(description = "음수량(ml)", example = "650", nullable = true)
    @Positive
    @Max(value = DAILY_MAX_WATER_ML, message = "하루에 기록 가능한 최대 음수량은 " + (DAILY_MAX_WATER_ML / 1000) + "리터입니다.")
    private final Integer waterIntake;

    @Schema(description = "음주 정보", requiredMode = REQUIRED)
    @NotNull(message = "음주 여부를 알려주세요.")
    @Size(max = ALCOHOL_TYPE_SIZE, message = "주류는 " + ALCOHOL_TYPE_SIZE + "개입니다.")
    @Valid
    private final List<AlcoholConsumptionParam> alcoholParams;

    @Schema(description = "흡연량(개비)", example = "6", nullable = true)
    @PositiveOrZero
    @Max(value = DAILY_MAX_CIGARETTES, message = "하루에 기록 가능한 최대 흡연량은 " + DAILY_MAX_CIGARETTES + "개비입니다.")
    private final Integer smokedCigarettes;

    @Schema(description = "기록 시각", example = "2025-05-01")
    @NotNull(message = "날짜를 지정해 주세요.")
    @PastOrPresent
    private final LocalDate recordedDate;

    public ActivityRequest(Integer waterIntake, List<AlcoholConsumptionParam> alcoholParams, Integer smokedCigarettes, LocalDate recordedDate) {
        this.waterIntake = waterIntake;
        this.alcoholParams = alcoholParams;
        this.smokedCigarettes = smokedCigarettes;
        this.recordedDate = recordedDate;
    }

    @Schema(hidden = true)
    @AssertFalse(message = "기록을 입력해 주세요.")
    private boolean isNotRecorded() {
        return waterIntake == null && smokedCigarettes == null && CollectionUtils.isEmpty(alcoholParams);
    }


    @Schema(description = "음주 정보")
    public record AlcoholConsumptionParam(
            @Schema(description = "마신 술 종류", example = "BEER")
            @NotNull
            AlcoholType alcoholType,

            @Schema(description = "음주량(ml)", example = "500")
            @Positive
            @Max(value = DAILY_MAX_ALCOHOL_ML, message = "하루에 기록 가능한 음주량은 최대 " + DAILY_MAX_ALCOHOL_ML + "ml입니다.")
            int intake
    ) {}
}
