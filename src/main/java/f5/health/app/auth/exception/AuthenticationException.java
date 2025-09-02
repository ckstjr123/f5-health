package f5.health.app.auth.exception;

import f5.health.app.common.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends ApiException {

    public AuthenticationException(AuthErrorCode errorCode) {
        super(errorCode);
    }

    public AuthenticationException(AuthErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
