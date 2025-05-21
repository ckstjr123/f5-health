package f5.health.app.controller.food;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

import static f5.health.app.entity.Food.FOOD_CODE_LENGTH;
import static f5.health.app.entity.meal.Meal.MENU_LIMIT_SIZE_PER_MEAL;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "먹은 음식 코드(foodCode)들로 음식 상세 정보 요청", requiredMode = REQUIRED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EatenMealFoodsRequest {

    @Schema($schema = "식사에서 먹은 음식 코드(foodCode) 집합")
    @NotNull(message = "요청할 음식들 코드를 전달해 주세요.")
    @Size(min = 1, max = MENU_LIMIT_SIZE_PER_MEAL, message = "식사당 기록 가능한 메뉴 최대 개수는 " + MENU_LIMIT_SIZE_PER_MEAL + "개입니다.")
    private Set<@Length(min = FOOD_CODE_LENGTH, max = FOOD_CODE_LENGTH) String> foodCodeSet;
}
