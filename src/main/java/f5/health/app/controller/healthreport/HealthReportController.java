package f5.health.app.controller.healthreport;

import f5.health.app.constant.AlcoholType;
import f5.health.app.constant.EnumModel;
import f5.health.app.constant.EnumModelMapper;
import f5.health.app.jwt.JwtMember;
import f5.health.app.service.healthreport.HealthReportService;
import f5.health.app.service.healthreport.vo.request.DateRangeQuery;
import f5.health.app.service.healthreport.vo.request.HealthReportRequest;
import f5.health.app.vo.healthreport.response.HealthReportResponse;
import f5.health.app.vo.member.response.HealthLifestyleScoreList;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/health/report")
@RequiredArgsConstructor
public class HealthReportController implements HealthReportApiDocs {

    private final HealthReportService reportService;
    private final EnumModelMapper enumMapper;

    @GetMapping("/alcohol-types")
    public List<? extends EnumModel> alcoholTypes() {
        return enumMapper.get(AlcoholType.class);
    }

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
    public HealthReportResponse submit(@AuthenticationPrincipal JwtMember loginMember,
                                       @RequestBody @Valid HealthReportRequest reportRequest) {
        return reportService.submit(loginMember.getId(), reportRequest);
    }

}
