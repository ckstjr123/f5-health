package f5.health.app.meal.service.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.validation.MenuSize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "식단 저장", requiredMode = REQUIRED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MealRequest {

    @Schema(description = "해당 식사로 섭취한 각 음식 코드(PK) 및 수량 리스트")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    @Valid
    @MenuSize
    private List<MealFoodParam> mealFoodParams = new ArrayList<>();

    @Schema(description = "식사 분류", example = "BREAKFAST")
    @NotNull(message = "식사 유형을 선택해 주세요.")
    private MealType mealType;

    @Schema(description = "식사 시각", example = "2025-05-07T07:31:28")
    @NotNull(message = "식사 시간대를 입력해 주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    private LocalDateTime eatenAt;


    public MealRequest(List<MealFoodParam> mealFoodParams, MealType mealType, LocalDateTime eatenAt) {
        this.mealFoodParams = mealFoodParams;
        this.mealType = mealType;
        this.eatenAt = eatenAt;
    }

    @Schema(hidden = true)
    public Set<String> getFoodCodes() {
        return mealFoodParams.stream()
                .map(MealFoodParam::getFoodCode)
                .collect(Collectors.toUnmodifiableSet());
    }
}
