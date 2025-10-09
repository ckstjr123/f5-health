package f5.health.app.meal.service.request;

import f5.health.app.common.validation.PrimaryKey;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.validation.MenuSize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
import static java.util.Objects.requireNonNullElseGet;

//TODO: ...
@Getter
@Schema(description = "식단 갱신 파라미터", requiredMode = REQUIRED)
@MenuSize
public final class MealSyncRequest {

    @Schema(description = "식사 분류", example = "BREAKFAST")
    @NotNull(message = "식사 유형을 선택해 주세요.")
    private final MealType mealType;

    @Schema(description = "식사 시각", example = "2025-05-07T07:31:28", nullable = true)
    @NotNull(message = "식사 시간대를 입력해 주세요.")
    @PastOrPresent
    private final LocalDateTime eatenAt;

    @Schema(description = "새로 추가된 식사 음식 항목")
    @Valid
    private final List<MealFoodParam> newMealFoodParams;

    @Schema(description = "기존 식사 음식에 대한 수정 사항")
    @Valid
    private final List<MealFoodUpdateParam> mealFoodUpdateParams;

    @Schema(description = "삭제할 식사 음식 id 목록")
    private final Set<@PrimaryKey Long> toDeleteMealFoodIds;

    public MealSyncRequest(MealType mealType, LocalDateTime eatenAt, List<MealFoodParam> newMealFoodParams, List<MealFoodUpdateParam> mealFoodUpdateParams, Set<Long> toDeleteMealFoodIds) {
        this.mealType = mealType;
        this.eatenAt = eatenAt;
        this.newMealFoodParams = requireNonNullElseGet(newMealFoodParams, ArrayList::new);
        this.mealFoodUpdateParams = requireNonNullElseGet(mealFoodUpdateParams, ArrayList::new);
        this.toDeleteMealFoodIds = requireNonNullElseGet(toDeleteMealFoodIds, HashSet::new);
    }


    @Schema(hidden = true)
    public Set<Long> getRequestedFoodIds() {
        return Stream.concat(
                newMealFoodParams.stream()
                        .map(MealFoodParam::foodId),
                mealFoodUpdateParams.stream()
                        .map(MealFoodUpdateParam::foodId)
        ).collect(Collectors.toUnmodifiableSet());
    }
}
