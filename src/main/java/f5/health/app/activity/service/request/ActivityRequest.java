package f5.health.app.activity.service.request;

import f5.health.app.activity.constant.AlcoholType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

import static f5.health.app.activity.constant.AlcoholType.ALCOHOL_TYPE_SIZE;
import static f5.health.app.activity.entity.Activity.*;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "활동 데이터 기록 요청 VO", requiredMode = REQUIRED)
public record ActivityRequest(
        @Schema(description = "음수량(ml)", example = "650") @Range(min = 0, max = DAILY_MAX_WATER_ML, message = "하루에 기록 가능한 최대 음수량은 " + (DAILY_MAX_WATER_ML / 1000) + "리터입니다.") int waterIntake,
        @Schema(description = "흡연량(개비)", example = "6") @Range(min = 0, max = DAILY_MAX_CIGARETTES, message = "하루에 기록 가능한 흡연량 최대치는 " + DAILY_MAX_CIGARETTES + "개비입니다.") int smokedCigarettes,
        @Schema(description = "음주 정보", requiredMode = REQUIRED) @NotNull(message = "음주 여부를 알려주세요.") @Size(max = ALCOHOL_TYPE_SIZE, message = "주류는 " + ALCOHOL_TYPE_SIZE + "개입니다.") @Valid List<AlcoholParam> alcoholParams,
        @Schema(description = "기록 시각", example = "2025-05-01 18:00") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @PastOrPresent LocalDate recordDate) {

    @Schema(hidden = true)
    public int alcoholIntake() {
        return alcoholParams.stream().mapToInt(AlcoholParam::alcoholIntake).sum();
    }


    @Schema(description = "음주 정보")
    public record AlcoholParam(@Schema(description = "마신 술 종류", example = "BEER") @NotNull AlcoholType alcoholType,
                               @Schema(description = "음주량(ml)", example = "500") @Range(min = 0, max = DAILY_MAX_ALCOHOL_ML, message = "하루에 기록 가능한 음주량은 최대 " + DAILY_MAX_ALCOHOL_ML + "ml입니다.") int alcoholIntake) {
    }
}
