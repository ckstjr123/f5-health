package f5.health.app.service.healthreport.vo.request;

import f5.health.app.constant.MealType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static f5.health.app.entity.Meal.MENU_LIMIT_SIZE_PER_MEAL;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@ToString
@Schema(description = "식사 정보", requiredMode = REQUIRED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MealRequest {

    @Schema(description = "해당 식사에 섭취한 각 음식 코드(PK) 및 수량 리스트")
    @Valid
    @NotNull(message = "식사를 최소 한개 기록해야 합니다.")
    @Size(min = 1, max = MENU_LIMIT_SIZE_PER_MEAL, message = "식사당 기록 가능한 메뉴 최대 개수는 " + MENU_LIMIT_SIZE_PER_MEAL + "개입니다.")
    private List<MealFoodRequest> mealFoodRequestList;

    @Schema(description = "식사 분류", example = "BREAKFAST")
    @NotNull
    private MealType mealType;

    @Schema(description = "식사 시각", example = "2025-05-07T07:31:28")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mealTime;

    public MealRequest(List<MealFoodRequest> mealFoodRequestList, MealType mealType, LocalDateTime mealTime) {
        this.mealFoodRequestList = mealFoodRequestList;
        this.mealType = mealType;
        this.mealTime = mealTime;
    }
}
