package f5.health.app.service.meal.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import static f5.health.app.entity.Meal.MEAL_TYPE_COUNT;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@ToString
@Schema(description = "식단(아침/점심/저녁/간식..) 기록 컬렉션", requiredMode = REQUIRED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MealsRequest {
    
    @Schema(description = "식단 리스트")
    @Valid
    @NotNull(message = "식단을 최소 한개 작성해 주세요.")
    @Size(min = 1, max = MEAL_TYPE_COUNT, message = "식사는 하루에 " + MEAL_TYPE_COUNT + "개로 분류됩니다.")
    private List<MealRequest> mealRequestList;
}
