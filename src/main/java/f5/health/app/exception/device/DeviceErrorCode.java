package f5.health.app.exception.device;

import f5.health.app.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum DeviceErrorCode implements ErrorCode {

    NOT_FOUND_DEVICE("등록되지 않은 기기입니다.");

    private final String code;
    private final String message;

    DeviceErrorCode(String message) {
        this.code = this.name();
        this.message = message;
    }
}
