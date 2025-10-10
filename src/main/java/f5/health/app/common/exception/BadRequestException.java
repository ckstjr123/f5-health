package f5.health.app.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static f5.health.app.common.exception.CommonErrorCode.INVALID_REQUEST;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends ApiException {

    public BadRequestException() {
        super(INVALID_REQUEST);
    }

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestException(String message) {
        super(INVALID_REQUEST, message);
    }
}
