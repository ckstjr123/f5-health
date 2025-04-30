package f5.health.app.exception.healthreport;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SizeLimitExceededException extends IllegalArgumentException {

    private final HealthReportErrorCode errorCode;

    public SizeLimitExceededException(HealthReportErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
