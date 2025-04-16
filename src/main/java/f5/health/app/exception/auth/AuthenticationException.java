package f5.health.app.exception.auth;

import lombok.Getter;

@Getter
public class AuthenticationException extends org.springframework.security.core.AuthenticationException {

    private final AuthErrorCode errorCode;

    public AuthenticationException(AuthErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
