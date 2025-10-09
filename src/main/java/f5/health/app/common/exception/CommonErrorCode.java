package f5.health.app.common.exception;

import lombok.Getter;

@Getter
public enum CommonErrorCode implements ErrorCode {

    INVALID_REQUEST("잘못된 요청입니다."),
    FORBIDDEN_REQUEST("허용되지 않은 요청입니다.");

    private final String code;
    private final String message;

    CommonErrorCode(String message) {
        this.code = this.name();
        this.message = message;
    }
}
