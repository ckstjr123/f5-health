package f5.health.app.service.healthreport.vo.request.healthkit.applekit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}


