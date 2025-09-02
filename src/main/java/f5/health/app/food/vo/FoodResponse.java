package f5.health.app.food.vo;

import f5.health.app.food.entity.Food;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "음식 상세 정보(영양성분 함량 기준)")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FoodResponse {

    @Schema(description = "음식명", example = "냉면")
    private final String foodName;

    @Schema(description = "열량(kcal)", example = "100")
    private final int kcal;

    @Schema(description = "탄수화물(g)", example = "13.15")
    private final double carbohydrate;

    @Schema(description = "단백질(g)", example = "2.5")
    private final double protein;

    @Schema(description = "지방(g)", example = "3.2")
    private final double fat;

    @Schema(description = "당류(g)", example = "0.82")
    private final double sugar;

    @Schema(description = "나트륨(mg)", example = "415")
    private final int natrium;

    @Schema(description = "영양성분 함량 기준량", example = "100g")
    private final String nutritionContentStdQuantity;

    @Schema(description = "식품 중량", example = "550g")
    private final String foodWeight;

    public static FoodResponse from(Food food) {
        return new FoodResponse(
                food.getFoodName(),
                food.getKcal(),
                food.getCarbohydrate(),
                food.getProtein(),
                food.getFat(),
                food.getSugar(),
                food.getNatrium(),
                food.getNutritionContentStdQuantity(),
                food.getFoodWeight());
    }
}
