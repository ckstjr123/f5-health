package f5.health.app.food.vo;

import f5.health.app.food.entity.Food;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "음식 검색 결과 컬렉션")
public class FoodSearchResponse {

    private final List<FoodSearchResult> result;

    public FoodSearchResponse(List<FoodSearchResult> result) {
        this.result = result;
    }


    @Getter
    @Schema(description = "음식 검색 결과(자세한 음식 정보는 음식 상세 조회 필요)")
    public static class FoodSearchResult {

        @Schema(description = "음식 코드", example = "D303-148170000-0001")
        private final String foodCode;

        @Schema(description = "음식명", example = "라면")
        private final String foodName;

        @Schema(description = "칼로리(kcal)", example = "550")
        private final int kcal;
        
        @Schema(description = "탄수화물(g)", example = "50.15")
        private final double carbohydrate;

        @Schema(description = "단백질(g)", example = "15.2")
        private final double protein;

        @Schema(description = "지방(g)", example = "6.2")
        private final double fat;

        @Schema(description = "중량", example = "250")
        private final double totalGram;

        @Schema(description = "제공 단위", example = "컵", nullable = true)
        private final String unit;

        public FoodSearchResult(Food food) {
            this.foodCode = food.getFoodCode();
            this.foodName = food.getFoodName();
            this.kcal = food.getKcal();
            this.carbohydrate = food.getCarbohydrate();
            this.protein = food.getProtein();
            this.fat = food.getFat();
            this.totalGram = food.getTotalGram();
            this.unit = food.getUnit();
        }
    }
}
