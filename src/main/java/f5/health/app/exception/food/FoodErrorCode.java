package f5.health.app.exception.food;

import f5.health.app.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum FoodErrorCode implements ErrorCode {

    NOT_FOUND_FOOD("음식을 찾지 못했습니다.");

    private final String code;
    private final String message;

    FoodErrorCode(String message) {
        this.code = this.name();
        this.message = message;
    }
}

