package f5.health.app.meal.service.request;

import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.validation.MenuSize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "식단 갱신 파라미터", requiredMode = REQUIRED)
@MenuSize
public record MealSyncRequest(@Schema(description = "갱신 식단 id", example = "1") @Positive long mealId,
                              @Schema(description = "새로 추가된 식사 음식 항목") @Valid List<MealFoodParam> newMealFoodParams,
                              @Schema(description = "기존 식사 음식에 대한 수정 사항(삭제 대상 제외)") @Valid List<MealFoodUpdateParam> mealFoodUpdateParams,
                              @Schema(description = "식사 분류", example = "BREAKFAST") @NotNull(message = "식사 유형을 선택해 주세요.") MealType mealType,
                              @Schema(description = "식사 시각", example = "2025-05-07T07:31:28", nullable = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @NotNull(message = "식사 시간대를 입력해 주세요.") @PastOrPresent LocalDateTime eatenAt) {

    public MealSyncRequest {
        newMealFoodParams = Objects.requireNonNullElseGet(newMealFoodParams, ArrayList::new);
        mealFoodUpdateParams = Objects.requireNonNullElseGet(mealFoodUpdateParams, ArrayList::new);
    }

    @Schema(hidden = true)
    public Set<String> getFoodCodes() {
        return Stream.concat(
                newMealFoodParams.stream()
                        .map(MealFoodParam::foodCode),
                mealFoodUpdateParams.stream()
                        .map(MealFoodUpdateParam::foodCode)
        ).collect(Collectors.toUnmodifiableSet());
    }
}
