package f5.health.app.meal.service.request;

import f5.health.app.meal.domain.MealType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "식단 갱신 파라미터", requiredMode = REQUIRED)
public final class MealSyncRequest {

    @Schema(description = "식사 분류", example = "BREAKFAST")
    @NotNull(message = "식사 유형을 선택해 주세요.")
    private final MealType mealType;

    @Schema(description = "식사 시각", example = "2025-05-07T07:31:28", nullable = true)
    @NotNull(message = "식사 시간대를 설정해 주세요.")
    @PastOrPresent
    private final LocalDateTime eatenAt;

    @Schema(description = "메뉴 갱신")
    @NotNull
    @Valid
    private final MealFoodSyncParam mealFoodSyncParam;

    public MealSyncRequest(MealType mealType, LocalDateTime eatenAt, MealFoodSyncParam mealFoodSyncParam) {
        this.mealType = mealType;
        this.eatenAt = eatenAt;
        this.mealFoodSyncParam = mealFoodSyncParam;
    }

    @Schema(hidden = true)
    public Set<Long> getRequestedFoodIds() {
        return mealFoodSyncParam.requestedFoodIds();
    }
}
