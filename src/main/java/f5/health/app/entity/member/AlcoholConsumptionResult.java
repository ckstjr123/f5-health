package f5.health.app.entity.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static f5.health.app.constant.AlcoholType.ALCOHOL_TYPE_SIZE;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "음주 결과")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlcoholConsumptionResult {

    @Valid
    @NotNull(message = "음주 결과를 입력해 주세요.")
    @Schema(description = "음주 목록", requiredMode = REQUIRED)
    @Size(min = 1, max = ALCOHOL_TYPE_SIZE, message = "주류는 " + ALCOHOL_TYPE_SIZE + "개입니다.")
    private List<AlcoholConsumption> result;


    @Schema(hidden = true)
    public int getTotalConsumedAlcoholDrinks() {
        return result.stream()
                .mapToInt(AlcoholConsumption::getConsumedAlcoholDrinks)
                .sum();
    }

    @Schema(hidden = true)
    public double getTotalAlcoholCost() {
        return result.stream()
                .mapToDouble(alcoholConsumption ->
                        alcoholConsumption.getConsumedAlcoholDrinks() * alcoholConsumption.getAlcoholType().pricePerML())
                .sum();
    }
}
