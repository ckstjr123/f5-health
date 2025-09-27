package f5.health.app.meal.controller.response;

import f5.health.app.food.vo.FoodResponse;
import f5.health.app.meal.entity.MealFood;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "식사 음식 및 수량 응답")
public class MealFoodResponse {

    @Schema(description = "식사한 음식 상세 정보")
    private final FoodResponse food;

    @Schema(description = "먹은 수량", example = "1.0")
    private final double count;

    public MealFoodResponse(MealFood mealFood) {
        this.food = FoodResponse.from(mealFood.getFood());
        this.count = mealFood.getCount();
    }
}
