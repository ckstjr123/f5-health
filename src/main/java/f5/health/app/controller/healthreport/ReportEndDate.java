package f5.health.app.controller.healthreport;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "리포트 조회 날짜", example = "2025-05-18", requiredMode = REQUIRED)
@RequiredArgsConstructor
public class ReportEndDate {

    @Schema(description = "리포트가 등록되는 날짜", example = "2025-05-18")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @PastOrPresent
    private final LocalDate date;

    @Schema(hidden = true)
    public LocalDate get() {
        return this.date;
    }
}
