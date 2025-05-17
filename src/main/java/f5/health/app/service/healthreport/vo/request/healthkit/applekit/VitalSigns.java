package f5.health.app.service.healthreport.vo.request.healthkit.applekit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Getter
@Schema(description = "활력 징후")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VitalSigns {

    @Schema(description = "심박수(bpm)", example = "75")
    @Positive
    private int heartRate;

    @Schema(description = "산소 포화도(%)", example = "80")
    @Range(min = 0, max = 100)
    private int oxygenSaturation;

    @Schema(description = "체온(℃)", example = "36.5")
    private double bodyTemperature;
}
