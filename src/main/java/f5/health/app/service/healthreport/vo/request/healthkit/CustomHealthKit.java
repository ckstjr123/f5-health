package f5.health.app.service.healthreport.vo.request.healthkit;

import f5.health.app.validation.member.AlcoholDrinkingInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import static f5.health.app.entity.member.Member.MemberCheckUp.DAILY_MAX_CIGARETTES;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "단순 기록 건강 데이터 추적", requiredMode = REQUIRED)
@AlcoholDrinkingInfo
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomHealthKit {

    private static final int DAILY_MAX_WATER_ML = 5000;
    private static final int DAILY_MAX_ALCOHOL_DRINKS = 20;

    @Schema(description = "음수량(ml)", example = "650")
    @Range(min = 0, max = DAILY_MAX_WATER_ML, message = "하루에 기록 가능한 최대 음수량은 " + (DAILY_MAX_WATER_ML / 1000) + "리터입니다.")
    private int waterIntake;

    @Schema(description = "흡연량(개비)", example = "6")
    @Range(min = 0, max = DAILY_MAX_CIGARETTES, message = "하루에 기록 가능한 흡연량 최대치는 " + DAILY_MAX_CIGARETTES + "개비입니다.")
    private int smokedCigarettes;

    @Schema(description = "음주량(잔)", example = "7")
    @Range(min = 0, max = DAILY_MAX_ALCOHOL_DRINKS, message = "하루에 기록 가능한 술잔은 최대 " + DAILY_MAX_ALCOHOL_DRINKS + "개입니다.")
    private int consumedAlcoholDrinks;

    @Schema(description = "음주 소비 금액", example = "4500")
    @PositiveOrZero
    private int alcoholCost;
}
