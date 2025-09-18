package f5.health.app.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends ApiException {

    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
