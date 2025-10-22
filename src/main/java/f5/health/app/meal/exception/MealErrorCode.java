package f5.health.app.meal.exception;

import f5.health.app.common.exception.ErrorCode;
import f5.health.app.meal.domain.MealType;
import lombok.Getter;

import static f5.health.app.meal.domain.MealType.SNACK;
import static f5.health.app.meal.domain.Meal.MENU_MAX_SIZE_PER_MEAL;
import static f5.health.app.meal.domain.Meal.MENU_MIN_SIZE_PER_MEAL;

@Getter
public enum MealErrorCode implements ErrorCode {

    REDUNDANT_REGULAR_MEAL("이미 등록된 식사가 있습니다."),
    EXCEED_MAX_SNACK_COUNT("당일 등록 가능한 간식 횟수를 초과했습니다."),
    NOT_FOUND_MEAL("기록된 식단이 존재하지 않습니다."),
    NOT_FOUND_MEAL_OWNERSHIP("등록한 식단이 아닙니다."),
    REPEATED_MENU_UPDATE("중복된 수정 메뉴 id가 포함되어 있습니다."),
    CANNOT_DELETE_EDITED_MENU("수정 사항이 있는 메뉴는 삭제할 수 없습니다."),
    NOT_ALLOWED_MENU_COUNT("식단 메뉴는 최소 " + MENU_MIN_SIZE_PER_MEAL + "개 이상이어야 하며 " + MENU_MAX_SIZE_PER_MEAL + "개까지 기록할 수 있습니다.");

    private final String code;
    private final String message;

    MealErrorCode(String message) {
        this.code = this.name();
        this.message = message;
    }

    public static MealErrorCode getLimitExceededErrorCodeFor(MealType mealType) {
        return mealType != SNACK ? REDUNDANT_REGULAR_MEAL : EXCEED_MAX_SNACK_COUNT;
    }
}
