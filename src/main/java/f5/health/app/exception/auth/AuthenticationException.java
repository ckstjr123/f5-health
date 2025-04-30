package f5.health.app.exception.auth;

import f5.health.app.exception.ErrorCode;
import f5.health.app.exception.global.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends ApiException {

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthenticationException(AuthErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
