package f5.health.app.service.healthreport;

import f5.health.app.repository.HealthReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthReportService {

    private final HealthReportRepository reportRepository;

}
