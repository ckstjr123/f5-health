package f5.health.app.exception.global;

import f5.health.app.exception.ErrorCode;

public class DuplicateEntityException extends BadRequestException {

    public DuplicateEntityException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DuplicateEntityException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
