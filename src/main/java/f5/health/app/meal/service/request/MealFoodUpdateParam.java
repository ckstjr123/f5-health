package f5.health.app.meal.service.request;

import f5.health.app.common.validation.PrimaryKey;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "섭취한 음식 및 수량 수정", requiredMode = REQUIRED)
public record MealFoodUpdateParam(@Schema(description = "수정할 식사 음식 id", example = "10") @PrimaryKey Long mealFoodId,
                                  @Schema(description = "음식 식별자", example = "1") @PrimaryKey Long foodId,
                                  @Schema(description = "식사량", example = "1.5") @DecimalMin(value = "0.25") double count) {
}
