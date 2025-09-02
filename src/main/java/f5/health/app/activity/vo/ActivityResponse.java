package f5.health.app.activity.vo;

import f5.health.app.activity.entity.Activity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Schema(description = "건강 일지 저장/조회 응답")
public class ActivityResponse {

    @Schema(description = "수분 섭취량(ml)", example = "650")
    private final int waterIntake;

    @Schema(description = "피운 담배", example = "6")
    private final int smokedCigarettes;

    @Schema(description = "음주량(ml)", example = "200")
    private final int alcoholIntake;
    
    @Schema(description = "기록일자", example = "2025-05-19")
    private final LocalDate recordDate;

    public ActivityResponse(Activity activity) {
        this.waterIntake = activity.getWaterIntake();
        this.smokedCigarettes = activity.getSmokedCigarettes();
        this.alcoholIntake = activity.getAlcoholIntake();
        this.recordDate = activity.getRecordDate();
    }
}
