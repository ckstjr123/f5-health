package f5.health.app.vo.meal.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "식단 응답")
public class MealsResponse {

    @Schema(description = "개인별 권장 칼로리", example = "2000")
    private final int recommendedCalories;
    private final List<MealResponse> mealResponseList;

    private MealsResponse(int recommendedCalories, List<MealResponse> mealResponseList) {
        this.recommendedCalories = recommendedCalories;
        this.mealResponseList = mealResponseList;
    }

    public static MealsResponse of(int recommendedCalories, List<MealResponse> mealResponseList) {
        return new MealsResponse(recommendedCalories, mealResponseList);
    }
}
