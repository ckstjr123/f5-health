package f5.health.app.activity.vo;

import f5.health.app.activity.constant.AlcoholType;
import f5.health.app.activity.entity.AlcoholConsumption;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "음주 결과")
public class AlcoholResult {

    @Schema(description = "주류", example = "BEER")
    private AlcoholType type;

    @Schema(description = "주류 title", example = "맥주")
    private String label;

    @Schema(description = "음주량(ml)", example = "550")
    private int alcoholIntake;

    public AlcoholResult(AlcoholConsumption alcoholConsumption) {
        this.type = alcoholConsumption.getAlcoholType();
        this.label = type.label();
        this.alcoholIntake = alcoholConsumption.getAlcoholIntake();
    }
}
