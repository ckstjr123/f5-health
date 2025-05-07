package f5.health.app.controller.food;

import f5.health.app.entity.Food;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Schema(description = "음식 검색 결과 컬렉션")
public class FoodSearchResponse {

    private final List<FoodSearchResult> results;

    public FoodSearchResponse(List<FoodSearchResult> results) {
        this.results = results;
    }


    @Getter
    @Schema(description = "음식 검색 결과(로컬에서 활용할 음식 데이터)")
    public static class FoodSearchResult {

        @Schema(description = "음식 PK(음식 상세 정보 조회 또는 리포트 기록 시 파라미터로 활용", example = "D303-148170000-0001")
        private final String foodCode;

        @Schema(description = "음식명(검색 결과와 기록된 식단에서 음식명 출력 용도)", example = "라면")
        private final String foodName;

        @Schema(description = "영양성분 함량 기준량에 따라 1인분으로 계산된 음식 칼로리(자세한 음식 정보 확인하려면 음식 상세 조회 필요)", example = "550")
        private final int kcal;


        public FoodSearchResult(Food food) {
            this.foodCode = food.getFoodCode();
            this.foodName = food.getFoodName();
            this.kcal = food.calculateOneServingKcal();
        }
    }
}
