package f5.health.app.vo.meal.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Schema(description = "식단 응답 컬렉션")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MealsResponse {

    private final List<MealResponse> mealResponseList;

    public static MealsResponse from(List<MealResponse> mealResponseList) {
        return new MealsResponse(mealResponseList);
    }
}
