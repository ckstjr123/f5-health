package f5.health.app.activity.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "활동 기록 저장")
public record CreateActivityResponse(@Schema(description = "저장된 활동 id", example = "1") Long activityId) {
}
