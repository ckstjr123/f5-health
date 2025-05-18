package f5.health.app.controller.healthreport;

import f5.health.app.jwt.JwtMember;
import f5.health.app.service.healthreport.HealthReportService;
import f5.health.app.service.healthreport.vo.request.HealthReportRequest;
import f5.health.app.vo.healthreport.response.HealthReportResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/health/report")
@RequiredArgsConstructor
public class HealthReportController implements HealthReportApiDocs {

    private final HealthReportService reportService;

    @GetMapping
    public HealthReportResponse findReport(@AuthenticationPrincipal JwtMember loginMember,
                                           @ModelAttribute("date") @Valid ReportEndDate date) {
        return this.reportService.findReport(loginMember.getId(), date.get());
    }

    @PostMapping("/submit")
    public HealthReportResponse submit(@AuthenticationPrincipal JwtMember loginMember,
                                       @RequestBody @Valid HealthReportRequest reportRequest) {
        return this.reportService.submit(loginMember.getId(), reportRequest);
    }

}
