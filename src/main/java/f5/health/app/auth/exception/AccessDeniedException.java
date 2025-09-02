package f5.health.app.auth.exception;

import f5.health.app.common.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static f5.health.app.auth.exception.AuthErrorCode.FORBIDDEN_REQUEST;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends ApiException {

    public AccessDeniedException(String message) {
        super(FORBIDDEN_REQUEST, message);
    }

    public AccessDeniedException(Throwable cause) {
        super(FORBIDDEN_REQUEST, FORBIDDEN_REQUEST.getMessage(), cause);
    }
}
