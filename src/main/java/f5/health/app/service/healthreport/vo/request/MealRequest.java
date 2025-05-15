package f5.health.app.service.healthreport.vo.request;

import f5.health.app.constant.meal.MealType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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

    @Schema(description = "해당 식사로 섭취한 각 음식 코드(PK) 및 수량 리스트")
    @Valid
    @NotNull(message = "식사한 음식은 최소 한개 이상이어야 합니다.")
    @Size(min = 1, max = MENU_LIMIT_SIZE_PER_MEAL, message = "식사당 기록 가능한 메뉴 최대 개수는 " + MENU_LIMIT_SIZE_PER_MEAL + "개입니다.")
    private List<MealFoodRequest> mealFoodsRequest;

    @Schema(description = "식사 분류", example = "BREAKFAST")
    @NotNull(message = "식사 유형을 선택해 주세요.")
    private MealType mealType;

    @Schema(description = "식사 시각", example = "2025-05-07T07:31:28", nullable = true)
//    @NotNull(message = "식사 시간대를 입력해 주세요.")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @PastOrPresent
    private LocalDateTime mealTime;
}
