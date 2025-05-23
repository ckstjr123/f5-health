package f5.health.app.exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class RefreshViolationException extends AuthenticationException {

    public RefreshViolationException(AuthErrorCode errorCode) {
        super(errorCode);
    }
}

