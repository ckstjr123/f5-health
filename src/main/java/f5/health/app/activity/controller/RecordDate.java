package f5.health.app.activity.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "기록 데이터 조회일자", example = "2025-05-18", requiredMode = REQUIRED)
public record RecordDate(
        @Schema(description = "기록일자", example = "2025-05-18")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @NotNull @PastOrPresent LocalDate date) {

    @Schema(hidden = true)
    public LocalDate get() {
        return this.date;
    }
}
