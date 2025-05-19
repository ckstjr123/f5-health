package f5.health.app.vo.member.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "건강 생활 점수")
public interface HealthLifeScore {

    @Schema(description = "점수", example = "90")
    int getHealthLifeScore();
}
