package f5.health.app.service.healthreport.vo.request;

import f5.health.app.vo.healthreport.request.healthkit.AppleHealthKit;
import f5.health.app.vo.healthreport.request.healthkit.CustomHealthKit;
import f5.health.app.vo.healthreport.request.healthkit.HealthKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


/** HealthReportController -> HealthReportService 전달용 VO */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HealthReportRequest {

    private HealthKit healthKit;
    private MealsRequest mealsRequest;

    public static HealthReportRequest of(HealthKit healthKit, MealsRequest mealsRequest) {
        HealthReportRequest reportRequest = new HealthReportRequest();
        reportRequest.healthKit = healthKit;
        reportRequest.mealsRequest = mealsRequest;
        return reportRequest;
    }

    public LocalDateTime getStartDateTime() {
        return healthKit.getPeriod().getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return healthKit.getPeriod().getEndDateTime();
    }

    public AppleHealthKit getAppleHealthKit() {
        return healthKit.getAppleHealthKit();
    }

    public CustomHealthKit getCustomHealthKit() {
        return healthKit.getCustomHealthKit();
    }
}
