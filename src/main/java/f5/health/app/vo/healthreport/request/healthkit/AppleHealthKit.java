package f5.health.app.vo.healthreport.request.healthkit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.util.Set;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "사용자 애플 건강 앱 데이터", requiredMode = REQUIRED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppleHealthKit {

    @NotNull(message = "사용자 신체적 활동 정보가 전달되지 않았습니다.")
    @Valid
    private Activity activity;

    @NotNull(message = "사용자 수면 분석 정보가 전달되지 않았습니다.")
    @Valid
    private SleepAnalysis sleepAnalysis;

    @NotNull(message = "사용자 활력 징후 정보가 전달되지 않았습니다.")
    @Valid
    private VitalSigns vitalSigns;

    @Valid
    private Workouts workouts;


    @Getter
    @Schema(description = "활동")
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class Activity {

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

        @Schema(description = "전신 움직임 소요 시간(분)", example = "30.5")
        @PositiveOrZero
        private double appleMoveTime;
    }


    @Getter
    @Schema(description = "수면 분석")
    static class SleepAnalysis {

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

    @Getter
    @Schema(description = "활력 징후")
    static class VitalSigns {

        @Schema(description = "심박수(bpm)", example = "75")
        @Positive
        private int heartRate;

        @Schema(description = "산소 포화도(%)", example = "80")
        @Range(min = 0, max = 100)
        private int oxygenSaturation;
        
        @Schema(description = "체온(℃)", example = "36.5")
        private double bodyTemperature;
    }

    @Getter
    @Schema(description = "운동", nullable = true)
    static class Workouts {

        @Schema(description = "사용자 운동 유형", example = "walking")
        private Set<String> workoutTypes;
    }
}
