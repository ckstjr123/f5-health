package f5.health.app.vo.meal.response;

import f5.health.app.constant.meal.MealType;
import f5.health.app.entity.Meal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Schema(description = "식사 상세 정보 응답")
public final class MealResponse {

    /**
     * 리포트 조회 시 나타나는 식단에는 식사한 음식 조회 X,
     * 식단 상세 정보(ex. 아침 식사 상세 정보) 조회 시
     * MealFoods(해당 식사에서 먹은 음식들 및 각 수량) 필요
     */
    @Schema(description = "식사 id", example = "1")
    private final Long mealId;

    @Schema(description = "식사한 음식들(리포트 조회 메인에선 null, 리포트에서 식단 상세 정보(ex. 아침 식사 상세 정보) 조회 시 응답", nullable = true)
    private final List<MealFoodResponse> mealFoodResponseList;

    @Schema(description = "식사 타입", example = "LUNCH")
    private final MealType mealType;

    @Schema(description = "식사 시간", example = "2025-05-14T12:30:45.123")
    private final LocalDateTime mealTime;

    @Schema(description = "식사 총 섭취 칼로리", example = "1800")
    private final int totalKcal;

    private MealResponse(Meal meal, boolean isNeedMealFoods) {
        this.mealId = meal.getId();
        this.mealFoodResponseList = isNeedMealFoods ?
                meal.getMealFoods().stream()
                        .map(mealFood -> new MealFoodResponse(mealFood))
                        .toList() : null;
        this.mealType = meal.getMealType();
        this.mealTime = meal.getMealTime();
        this.totalKcal = meal.getTotalKcal();
    }

    public static MealResponse only(Meal meal) {
        return new MealResponse(meal, false);
    }

    public static MealResponse withMealFoods(Meal meal) {
        return new MealResponse(meal, true);
    }
}
