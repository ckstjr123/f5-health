package f5.health.app.meal.exception;

import f5.health.app.common.exception.ErrorCode;
import f5.health.app.meal.constant.MealType;
import lombok.Getter;

import static f5.health.app.meal.constant.MealType.SNACK;
import static f5.health.app.meal.domain.Meal.MENU_LIMIT_SIZE_PER_MEAL;

@Getter
public enum MealErrorCode implements ErrorCode {

    REDUNDANT_REGULAR_MEAL("이미 등록된 식사가 있습니다."),
    EXCEEDED_MAX_SNACK_COUNT("당일 등록 가능한 간식 횟수를 초과했습니다."),
    NOT_FOUND_MEAL("기록된 식단이 존재하지 않습니다."),
    NOT_FOUND_MEAL_OWNERSHIP("등록한 식단이 아닙니다."),
    EXCEEDED_MAX_MENU_LIMIT("식사당 기록 가능한 메뉴는 최대 " + MENU_LIMIT_SIZE_PER_MEAL + "개입니다.");

    private final String code;
    private final String message;

    MealErrorCode(String message) {
        this.code = this.name();
        this.message = message;
    }

    public static MealErrorCode mealLimitExceededErrorCodeFor(MealType mealType) {
        return mealType != SNACK ? REDUNDANT_REGULAR_MEAL : EXCEEDED_MAX_SNACK_COUNT;
    }
}
