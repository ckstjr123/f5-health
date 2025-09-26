package f5.health.app.meal.service.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import static f5.health.app.food.entity.Food.FOOD_CODE_LENGTH;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "섭취한 음식 및 수량 수정", requiredMode = REQUIRED)
public record MealFoodUpdateParam(@Schema(description = "식사 음식 id", example = "10") @Positive long mealFoodId,
                                  @Schema(description = "음식 코드(PK)", example = "D402-145000000-0001") @Length(min = FOOD_CODE_LENGTH, max = FOOD_CODE_LENGTH) String foodCode,
                                  @Schema(description = "먹은 수량", example = "1") @DecimalMin(value = "0.25") double count) {
}
