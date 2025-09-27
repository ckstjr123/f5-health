package f5.health.app.meal.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "식단 응답")
public class MealsResponse {

    private final List<MealResponse> meals;

    private MealsResponse(List<MealResponse> mealResponseList) {
        this.meals = mealResponseList;
    }

    public static MealsResponse from(List<MealResponse> mealResponseList) {
        return new MealsResponse(mealResponseList);
    }
}
