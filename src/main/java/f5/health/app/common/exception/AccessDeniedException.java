package f5.health.app.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static f5.health.app.common.exception.CommonErrorCode.FORBIDDEN_REQUEST;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends ApiException {

    public AccessDeniedException() {
        super(FORBIDDEN_REQUEST);
    }

    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
