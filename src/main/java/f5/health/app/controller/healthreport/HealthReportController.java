package f5.health.app.controller.healthreport;

import f5.health.app.service.healthreport.HealthReportService;
import f5.health.app.service.healthreport.vo.request.MealsRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // 인증 필터 url 임시 제외
    @PostMapping("/submit")
    public void submit(@RequestBody @Valid MealsRequest mealsRequest) {
        log.info("식단 toString(): {}", mealsRequest);
        log.info("식사 횟수: {}", mealsRequest.getMealRequestList().size());
        log.info("식사당 메뉴 기록 개수: {}", mealsRequest.getMealRequestList().stream().map(meal -> meal.getMealFoodRequestList().size()).toList());
    }

}
