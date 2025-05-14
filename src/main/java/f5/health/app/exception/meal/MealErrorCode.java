package f5.health.app.exception.meal;

import f5.health.app.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum MealErrorCode implements ErrorCode {

    NOT_FOUND_MEAL("기록된 식단이 존재하지 않습니다.");

    private final String code;
    private final String message;

    MealErrorCode(String message) {
        this.code = this.name();
        this.message = message;
    }
}
