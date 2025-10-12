package f5.health.app.activity.vo;

import f5.health.app.activity.domain.Activity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Schema(description = "활동 기록 조회")
public class ActivityResponse {

    @Schema(description = "활동 id", example = "1")
    private final Long activityId;

    @Schema(description = "수분 섭취량(ml)", example = "650")
    private final Integer waterIntake;

    @Schema(description = "피운 담배", example = "6")
    private final Integer smokedCigarettes;

    @Schema(description = "음주 정보")
    private final List<AlcoholConsumptionResult> alcoholConsumptionResult;

    @Schema(description = "기록일자", example = "2025-10-05")
    private final LocalDate recordDate;

    private ActivityResponse(Activity activity) {
        this.activityId = activity.getId();
        this.waterIntake = activity.getWaterIntake();
        this.smokedCigarettes = activity.getSmokedCigarettes();
        this.alcoholConsumptionResult = activity.getAlcoholConsumptions().stream()
                .map(AlcoholConsumptionResult::new)
                .toList();
        this.recordDate = activity.getRecordDate();
    }

    public static ActivityResponse from(Activity activity) {
        return new ActivityResponse(activity);
    }
}
