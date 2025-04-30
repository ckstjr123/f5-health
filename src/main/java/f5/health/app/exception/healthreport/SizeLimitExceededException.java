package f5.health.app.exception.healthreport;

import f5.health.app.exception.global.BadRequestException;

public class SizeLimitExceededException extends BadRequestException {

    public SizeLimitExceededException(HealthReportErrorCode errorCode) {
        super(errorCode);
    }
}
