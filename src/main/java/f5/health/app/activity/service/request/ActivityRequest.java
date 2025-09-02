package f5.health.app.activity.service.request;

import f5.health.app.member.entity.AlcoholConsumptionInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static f5.health.app.member.entity.Member.CheckUp.DAILY_MAX_CIGARETTES;
import static f5.health.app.member.entity.Member.CheckUp.DAILY_MAX_WATER_ML;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "활동 데이터 기록 요청 VO", requiredMode = REQUIRED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActivityRequest {

    @Schema(description = "음수량(ml)", example = "650")
    @Range(min = 0, max = DAILY_MAX_WATER_ML, message = "하루에 기록 가능한 최대 음수량은 " + (DAILY_MAX_WATER_ML / 1000) + "리터입니다.")
    private int waterIntake;

    @Schema(description = "흡연량(개비)", example = "6")
    @Range(min = 0, max = DAILY_MAX_CIGARETTES, message = "하루에 기록 가능한 흡연량 최대치는 " + DAILY_MAX_CIGARETTES + "개비입니다.")
    private int smokedCigarettes;

    @Schema(description = "음주 관련 정보", requiredMode = NOT_REQUIRED)
    @NotNull(message = "음주 여부를 알려주세요.")
    @Valid
    private AlcoholConsumptionInfo alcoholConsumptionInfo;

    @Schema(description = "기록 시각", example = "2025-05-01 18:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @PastOrPresent
    private LocalDate recordDate;

    @Builder(builderMethodName = "createActivityRequest")
    private ActivityRequest(int waterIntake, int smokedCigarettes, AlcoholConsumptionInfo alcoholConsumptionInfo, LocalDate recordDate) {
        this.waterIntake = waterIntake;
        this.smokedCigarettes = smokedCigarettes;
        this.alcoholConsumptionInfo = alcoholConsumptionInfo;
        this.recordDate = recordDate;
    }

    @Schema(hidden = true)
    public int getAlcoholIntake() {
        return alcoholConsumptionInfo.getTotalAlcoholIntake();
    }
}
