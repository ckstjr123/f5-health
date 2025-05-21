package f5.health.app.service.healthreport.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import static f5.health.app.entity.Food.FOOD_CODE_LENGTH;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@ToString
@Schema(description = "섭취한 음식 및 수량", requiredMode = REQUIRED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MealFoodRequest {

    @Schema(description = "음식 코드(PK)", example = "D402-145000000-0001")
    @Length(min = FOOD_CODE_LENGTH, max = FOOD_CODE_LENGTH)
    private String foodCode;

    @Schema(description = "먹은 수량", example = "1")
    @DecimalMin(value = "0.25")
    private double count;
}