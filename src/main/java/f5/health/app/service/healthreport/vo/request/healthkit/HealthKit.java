package f5.health.app.service.healthreport.vo.request.healthkit;

import f5.health.app.service.healthreport.vo.request.healthkit.applekit.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "건강 관련 데이터")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HealthKit {

    @NotNull(message = "건강 데이터가 수집된 기간을 알 수 없습니다.")
    @Valid
    private HealthKit.Period period;

    @NotNull(message = "애플 건강 앱 데이터가 전달되지 않았습니다.")
    @Valid
    private AppleHealthKit appleHealthKit;

    @NotNull(message = "사용자 입력 건강 데이터가 전달되지 않았습니다.")
    @Valid
    private CustomHealthKit customHealthKit;


    @Schema(hidden = true)
    public Activity getActivity() {
        return appleHealthKit.getActivity();
    }

    @Schema(hidden = true)
    public SleepAnalysis getSleepAnalysis() {
        return appleHealthKit.getSleepAnalysis();
    }

    @Schema(hidden = true)
    public VitalSigns getVitalSigns() {
        return appleHealthKit.getVitalSigns();
    }

    @Schema(hidden = true)
    public Workouts getWorkouts() {
        return appleHealthKit.getWorkouts();
    }

    @Schema(hidden = true)
    public int getWaterIntake() {
        return customHealthKit.getWaterIntake();
    }

    @Schema(hidden = true)
    public int getSmokedCigarettes() {
        return customHealthKit.getSmokedCigarettes();
    }

    @Schema(hidden = true)
    public int getConsumedAlcoholDrinks() {
        return customHealthKit.getConsumedAlcoholDrinks();
    }

    @Schema(hidden = true)
    public int getAlcoholCost() {
        return customHealthKit.getAlcoholCost();
    }


    @Getter
    @Schema(description = "건강 관측 기간", requiredMode = REQUIRED)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Period {

        @Schema(description = "건강 데이터 수집 시작 날짜(yyyy-MM-dd HH:mm)", example = "2025-04-30 00:10")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        @PastOrPresent
        private LocalDateTime startDateTime;

        @Schema(description = "건강 데이터 수집 종료 날짜(리포트 기록 알림 날짜, yyyy-MM-dd HH:mm)", example = "2025-05-01 00:10")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        @PastOrPresent
        private LocalDateTime endDateTime;
    }
}
