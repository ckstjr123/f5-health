package f5.health.app.meal.vo.response;

import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.domain.Meal;
import f5.health.app.meal.domain.embedded.Nutrients;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Schema(description = "식단 상세 정보 응답")
public final class MealDetail {

    @Schema(description = "식단 id", example = "1")
    private final Long mealId;

    @Schema(description = "식사 음식 목록")
    private final List<MealFoodResponse> mealFoods;

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

    private MealDetail(Meal meal) {
        this.mealId = meal.getId();
        this.mealFoods = meal.getMealFoods().stream()
                .map(MealFoodResponse::new)
                .toList();
        this.type = meal.getMealType();
        this.label = type.label();
        this.eatenAt = meal.getEatenAt();
        Nutrients nutrients = meal.getNutrients();
        this.totalKcal = nutrients.getKcal();
        this.totalCarbohydrate = nutrients.getCarbohydrate();
        this.totalProtein = nutrients.getProtein();
        this.totalFat = nutrients.getFat();
    }

    public static MealDetail from(Meal meal) {
        return new MealDetail(meal);
    }
}