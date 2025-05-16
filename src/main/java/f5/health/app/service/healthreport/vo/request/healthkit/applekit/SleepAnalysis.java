package f5.health.app.service.healthreport.vo.request.healthkit.applekit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "수면 분석")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SleepAnalysis {

    @Schema(description = "침대에서 보낸 시간", example = "8")
    @PositiveOrZero
    private int inBed;

    @Schema(description = "깨어있는 시간", example = "16")
    @PositiveOrZero
    private int awake;

    @Schema(description = "가벼운 수면이나 중간 정도의 수면 시간", example = "4")
    @PositiveOrZero
    private int asleepCore;

    @Schema(description = "깊은 수면 시간", example = "3")
    @PositiveOrZero
    private int asleepDeep;

    @Schema(description = "렘 수면 시간", example = "0")
    @PositiveOrZero
    private int asleepREM;
}
