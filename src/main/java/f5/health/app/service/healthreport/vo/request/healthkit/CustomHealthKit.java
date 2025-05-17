package f5.health.app.service.healthreport.vo.request.healthkit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "단순 기록 건강 데이터 추적", requiredMode = REQUIRED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomHealthKit {

    private static final int DAILY_MAX_WATER_ML = 5000;
    private static final int DAILY_MAX_CIGARETTES = 35;
    private static final int DAILY_MAX_ALCOHOL_DRINKS = 15;

    @Schema(description = "음수량(ml)", example = "650")
    @Range(min = 0, max = DAILY_MAX_WATER_ML, message = "하루에 기록 가능한 최대 음수량은 " + (DAILY_MAX_WATER_ML / 1000) + "리터입니다.")
    private int waterIntake;

    @Schema(description = "흡연량(개비)", example = "6")
    @Range(min = 0, max = DAILY_MAX_CIGARETTES, message = "하루에 기록 가능한 흡연량 최대치는 " + DAILY_MAX_CIGARETTES + "개비입니다.")
    private int smokedCigarettes;

    @Schema(description = "알코올 섭취량(잔)", example = "3")
    @Range(min = 0, max = DAILY_MAX_ALCOHOL_DRINKS, message = "하루에 기록 가능한 술잔은 최대 " + DAILY_MAX_ALCOHOL_DRINKS + "개입니다.")
    private int consumedAlcoholDrinks;
}
