package f5.health.app.meal.service.request;

import f5.health.app.common.validation.PrimaryKey;
import f5.health.app.meal.validation.MenuSize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "식단 메뉴 갱신 파라미터", requiredMode = REQUIRED)
@MenuSize
public record MealFoodSyncParam(@Schema(description = "새로 추가된 식사 음식 항목") @NotNull @Valid List<MealFoodParam> newParams,
                                @Schema(description = "기존 식사 음식에 대한 수정 사항") @NotNull @Valid List<MealFoodUpdateParam> updateParams,
                                @Schema(hidden = true, description = "업데이트 대상 id 수집용 필드") Set<Long> updateIds,
                                @Schema(description = "삭제할 식사 메뉴 id 목록") @NotNull Set<@PrimaryKey Long> deleteIds) {

    public MealFoodSyncParam {
        updateIds = updateParams.stream()
                .map(MealFoodUpdateParam::mealFoodId)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Schema(hidden = true, description = "수정 id 중복 여부")
    public boolean hasDuplicateUpdate() {
        return updateIds.size() != updateParams.size();
    }

    @Schema(hidden = true, description = "요청 음식 id 모음")
    public Set<Long> requestedFoodIds() {
        return Stream.concat(
                newParams.stream()
                        .map(MealFoodParam::foodId),
                updateParams.stream()
                        .map(MealFoodUpdateParam::foodId)
        ).collect(Collectors.toUnmodifiableSet());
    }

    @Schema(hidden = true, description = "수정/삭제 요청 id")
    public Set<Long> requestedMealFoodIds() {
        return Stream.concat(updateIds.stream(), deleteIds.stream())
                .collect(Collectors.toUnmodifiableSet());
    }
}
