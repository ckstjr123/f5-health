package f5.health.app.controller.healthreport;

import f5.health.app.jwt.JwtMember;
import f5.health.app.service.healthreport.HealthReportService;
import f5.health.app.service.healthreport.vo.request.HealthReportRequest;
import f5.health.app.service.healthreport.vo.request.MealsRequest;
import f5.health.app.vo.healthreport.request.healthkit.HealthKit;
import f5.health.app.vo.healthreport.response.HealthReportResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/health/report")
@RequiredArgsConstructor
public class HealthReportController implements HealthReportApiDocs {

    private final HealthReportService reportService;

    @PostMapping("/submit") //()
    public HealthReportResponse submit(@AuthenticationPrincipal JwtMember loginMember,
                                       @RequestBody @Valid HealthKit healthKit, @RequestBody @Valid MealsRequest mealsRequest) {
        return this.reportService.submit(loginMember.getId(), HealthReportRequest.of(healthKit, mealsRequest));
    }

}
