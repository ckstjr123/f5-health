package f5.health.app.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends ApiException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorCode errorCode, String... ids) {
        super(errorCode, errorCode.getMessage() + " (id: {" + String.join(", ", ids) + "})");
    }

    public NotFoundException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
