package f5.health.app.meal.service.request;

import f5.health.app.meal.domain.MealType;
import f5.health.app.meal.validation.MenuSize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "식단 저장", requiredMode = REQUIRED)
public record MealRequest(
        @Schema(description = "해당 식사로 섭취한 각 음식 id(PK) 및 수량 리스트") @NotNull @MenuSize @Valid List<MealFoodParam> mealFoodParams,
        @Schema(description = "식사 분류", example = "BREAKFAST") @NotNull(message = "식사 유형을 선택해 주세요.") MealType mealType,
        @Schema(description = "식사 시각", example = "2025-05-07T07:31:28") @NotNull(message = "식사 시간대를 입력해 주세요.") @PastOrPresent LocalDateTime eatenAt) {

    @Schema(hidden = true)
    public Set<Long> getRequestedFoodIds() {
        return mealFoodParams.stream()
                .map(MealFoodParam::foodId)
                .collect(Collectors.toUnmodifiableSet());
    }
}
