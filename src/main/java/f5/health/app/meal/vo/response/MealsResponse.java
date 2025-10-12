package f5.health.app.meal.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "식사 요약 목록")
public class MealsResponse {

    private final List<MealSummary> meals;

    private MealsResponse(List<MealSummary> mealSummaries) {
        this.meals = mealSummaries;
    }

    public static MealsResponse from(List<MealSummary> mealSummaries) {
        return new MealsResponse(mealSummaries);
    }
}
