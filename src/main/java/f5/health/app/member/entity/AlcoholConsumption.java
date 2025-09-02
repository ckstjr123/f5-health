package f5.health.app.member.entity;

import f5.health.app.activity.constant.AlcoholType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import static f5.health.app.member.entity.Member.CheckUp.DAILY_MAX_ALCOHOL_ML;

@Getter
@Schema(description = "음주 정보", nullable = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlcoholConsumption {

    @Schema(description = "마신 술 종류", example = "BEER")
    @NotNull
    private AlcoholType alcoholType;

    @Schema(description = "음주량(ml)", example = "500")
    @Range(min = 0, max = DAILY_MAX_ALCOHOL_ML, message = "하루에 기록 가능한 음주량은 최대 " + DAILY_MAX_ALCOHOL_ML + "ml입니다.")
    private int alcoholIntake;
}
