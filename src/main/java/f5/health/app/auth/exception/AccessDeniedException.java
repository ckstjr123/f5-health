package f5.health.app.auth.exception;

import f5.health.app.common.exception.ApiException;
import f5.health.app.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static f5.health.app.auth.exception.AuthErrorCode.FORBIDDEN_REQUEST;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends ApiException {

    public AccessDeniedException() {
        super(FORBIDDEN_REQUEST);
    }

    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
