package f5.health.app.meal.service.request;

import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.validation.MenuSize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "식단 갱신 파라미터", requiredMode = REQUIRED)
@MenuSize
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MealSyncRequest {

    @Schema(description = "갱신 식단 id", example = "1")
    @Positive
    private long mealId;

    @Schema(description = "새로 추가된 식사 음식 항목")
    @Valid
    @NotNull
    private List<MealFoodParam> newMealFoodParams;

    @Schema(description = "기존 식사 음식에 대한 수정 사항(삭제 대상 제외)")
    @Valid
    @NotNull // TODO: null로 요청
//    @Size(min = MENU_MIN_SIZE_PER_MEAL, message = "수정할 식사 음식을 전달해 주세요.")
    private List<MealFoodUpdateParam> mealFoodUpdateParams;

    @Schema(description = "식사 분류", example = "BREAKFAST")
    @NotNull(message = "식사 유형을 선택해 주세요.")
    private MealType mealType;

    @Schema(description = "식사 시각", example = "2025-05-07T07:31:28", nullable = true)
    @NotNull(message = "식사 시간대를 입력해 주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    private LocalDateTime eatenAt;


    @Schema(hidden = true)
    public Set<String> getFoodCodes() {
        return Stream.concat(
                newMealFoodParams.stream()
                        .map(MealFoodParam::getFoodCode),
                mealFoodUpdateParams.stream()
                        .map(MealFoodUpdateParam::getFoodCode)
        ).collect(Collectors.toUnmodifiableSet());
    }
}
