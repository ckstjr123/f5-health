package f5.health.app.vo.member.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "건강 생활 점수")
public interface HealthLifestyleScore {

    @Schema(description = "점수", example = "90")
    int getHealthLifeScore();

    @Schema(description = "점수 받은 날짜", example = "2025-05-21")
    LocalDate getEndDate();
}