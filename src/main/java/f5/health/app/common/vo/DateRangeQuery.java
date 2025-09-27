package f5.health.app.common.vo;

import f5.health.app.common.validation.DateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "날짜 범위 조회 요청", requiredMode = REQUIRED)
@DateRange(maxDays = 30, message = "조회 날짜 범위는 최대 31일까지만 허용됩니다.")
public record DateRangeQuery(
        @Schema(description = "조회 시작 날짜", example = "2025-05-01") @DateTimeFormat(pattern = "yyyy-MM-dd") @NotNull(message = "조회 시작 날짜를 지정해 주세요.") @PastOrPresent LocalDate start,
        @Schema(description = "조회 끝 날짜", example = "2025-05-31") @DateTimeFormat(pattern = "yyyy-MM-dd") @NotNull(message = "조회 날짜 끝을 지정해 주세요.") @PastOrPresent LocalDate end) {
}
