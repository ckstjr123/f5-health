package f5.health.app.controller.food;

import f5.health.app.entity.Food;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "음식 응답 컬렉션")
public class FoodsResponse {

    @Schema(description = "음식 리스트")
    private final List<FoodResponse> foodResponseList;

    private FoodsResponse(List<Food> foods) {
        this.foodResponseList = foods.stream()
                .map(food -> FoodResponse.from(food))
                .toList();
    }
    public static FoodsResponse from(List<Food> foods) {
        return new FoodsResponse(foods);
    }
}
