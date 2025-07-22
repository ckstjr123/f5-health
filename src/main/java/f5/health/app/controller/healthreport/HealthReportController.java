package f5.health.app.controller.healthreport;

import f5.health.app.jwt.JwtMember;
import f5.health.app.service.healthreport.HealthReportService;
import f5.health.app.service.healthreport.vo.request.DateRangeQuery;
import f5.health.app.service.healthreport.vo.request.HealthReportRequest;
import f5.health.app.vo.healthreport.response.HealthReportResponse;
import f5.health.app.vo.member.response.HealthLifestyleScoreList;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/health/report")
@RequiredArgsConstructor
public class HealthReportController implements HealthReportApiDocs {

    private final HealthReportService reportService;

    @GetMapping
    public HealthReportResponse report(@AuthenticationPrincipal JwtMember loginMember,
                                       @ModelAttribute("date") @Valid ReportEndDate date) {
        return reportService.findReport(loginMember.getId(), date.get());
    }

    @GetMapping("/scores")
    public HealthLifestyleScoreList scores(@AuthenticationPrincipal JwtMember loginMember,
                                           @RequestBody @Valid DateRangeQuery dateRangeQuery) {
        return reportService.findScores(loginMember, dateRangeQuery);
    }

    @PostMapping("/submit")
    public ResponseEntity<Void> submit(@AuthenticationPrincipal JwtMember loginMember,
                                 @RequestBody @Valid HealthReportRequest reportRequest) {
        reportService.submit(loginMember.getId(), reportRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
