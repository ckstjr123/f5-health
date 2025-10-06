package f5.health.app.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends ApiException {

    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
