package f5.health.app.service.healthreport.vo.request.healthkit.applekit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "활동")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Activity {

    @Schema(description = "걸음 수", example = "3800")
    @PositiveOrZero
    private int stepCount;

    @Schema(description = "걷거나 달려서 이동한 거리(m)", example = "100.5")
    @PositiveOrZero
    private double distanceWalkingRunning;

    @Schema(description = "소모한 활성 에너지(kcal)", example = "450")
    @PositiveOrZero
    private double activeEnergyBurned;

    @Schema(description = "운동 시간(분)", example = "42.5")
    @PositiveOrZero
    private double appleExerciseTime;
}
