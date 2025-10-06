package f5.health.app.meal.controller.response;

import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.entity.Meal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "식사 요약")
public final class MealSummary {

    @Schema(description = "식사 id", example = "1")
    private final Long id;

    @Schema(description = "식사 타입 value", example = "DINNER")
    private final MealType type;

    @Schema(description = "식사 타입 label", example = "저녁")
    private final String label;

    @Schema(description = "식사 시각", example = "2025-05-14T20:30:45.123")
    private final LocalDateTime eatenAt;

    @Schema(description = "식사 총 섭취 칼로리", example = "1800")
    private final int totalKcal;

    @Schema(description = "식사 탄수화물", example = "100.5")
    private final double totalCarbohydrate;

    @Schema(description = "식사 단백질", example = "56.0")
    private final double totalProtein;

    @Schema(description = "식사 지방", example = "48.2")
    private final double totalFat;

    private MealSummary(Meal meal) {
        this.id = meal.getId();
        this.type = meal.getMealType();
        this.label = type.label();
        this.eatenAt = meal.getEatenAt();
        this.totalKcal = meal.getTotalKcal();
        this.totalCarbohydrate = meal.getTotalCarbohydrate();
        this.totalProtein = meal.getTotalProtein();
        this.totalFat = meal.getTotalFat();
    }

    public static MealSummary from(Meal meal) {
        return new MealSummary(meal);
    }
}
