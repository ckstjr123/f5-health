package f5.health.app.service.healthreport.vo.request;

import f5.health.app.service.healthreport.vo.request.healthkit.HealthKit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;


/** HealthReportController -> HealthReportService 전달용 VO */
@Getter
@Schema(description = "리포트 등록 요청 VO", requiredMode = REQUIRED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HealthReportRequest {

    @NotNull(message = "건강 수집 데이터가 전송되지 않았습니다.")
    @Valid
    private HealthKit healthKit;

    @NotNull(message = "식단을 기록해 주세요.")
    @Valid
    private MealsRequest mealsRequest;


    @Schema(hidden = true)
    public LocalDateTime getStartDateTime() {
        return healthKit.getPeriod().getStartDateTime();
    }

    @Schema(hidden = true)
    public LocalDateTime getEndDateTime() {
        return healthKit.getPeriod().getEndDateTime();
    }
}
