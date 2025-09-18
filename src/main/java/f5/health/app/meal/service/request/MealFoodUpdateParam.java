package f5.health.app.meal.service.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static f5.health.app.food.entity.Food.FOOD_CODE_LENGTH;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "섭취한 음식 및 수량 수정", requiredMode = REQUIRED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MealFoodUpdateParam {

    @Schema(description = "식사 음식 id", example = "10")
    @Positive
    private long mealFoodId;

    @Schema(description = "음식 코드(PK)", example = "D402-145000000-0001")
    @Length(min = FOOD_CODE_LENGTH, max = FOOD_CODE_LENGTH)
    private String foodCode;

    @Schema(description = "먹은 수량", example = "1")
    @DecimalMin(value = "0.25")
    private double count;
}
