package f5.health.app.service.healthreport.vo.request;

import f5.health.app.vo.healthreport.request.healthkit.HealthKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** HealthReportController -> HealthReportService 전달용 VO */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReportRequest {
    private HealthKit healthKit;
    private  MealsRequest mealsRequest;

    public static ReportRequest of(HealthKit healthKit, MealsRequest mealsRequest) {
        ReportRequest reportRequest = new ReportRequest();
        reportRequest.healthKit = healthKit;
        reportRequest.mealsRequest = mealsRequest;
        return reportRequest;
    }
}
