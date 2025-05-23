package f5.health.app.controller.food;

import f5.health.app.entity.Food;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "음식 응답 컬렉션")
public class FoodsResponse {

    @Schema(description = "개인별 권장 칼로리", example = "2000")
    private final int recommendedCalories;

    @Schema(description = "음식 리스트")
    private final List<FoodResponse> foodResponseList;

    private FoodsResponse(int recommendedCalories,List<Food> foods) {
        this.recommendedCalories = recommendedCalories;
        this.foodResponseList = foods.stream()
                .map(food -> FoodResponse.from(food))
                .toList();
    }
    public static FoodsResponse of(int recommendedCalories, List<Food> foods) {
        return new FoodsResponse(recommendedCalories, foods);
    }
}
