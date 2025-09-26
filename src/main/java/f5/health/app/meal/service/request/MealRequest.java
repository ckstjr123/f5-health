package f5.health.app.meal.service.request;

import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.validation.MenuSize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "식단 저장", requiredMode = REQUIRED)
public record MealRequest(
        @Schema(description = "해당 식사로 섭취한 각 음식 코드(PK) 및 수량 리스트") @MenuSize @Valid List<MealFoodParam> mealFoodParams,
        @Schema(description = "식사 분류", example = "BREAKFAST") @NotNull(message = "식사 유형을 선택해 주세요.") MealType mealType,
        @Schema(description = "식사 시각", example = "2025-05-07T07:31:28") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @NotNull(message = "식사 시간대를 입력해 주세요.") @PastOrPresent LocalDateTime eatenAt) {

    public MealRequest {
        mealFoodParams = Objects.requireNonNullElseGet(mealFoodParams, ArrayList::new);
    }

    @Schema(hidden = true)
    public Set<String> getFoodCodes() {
        return mealFoodParams.stream()
                .map(MealFoodParam::foodCode)
                .collect(Collectors.toUnmodifiableSet());
    }
}
