package f5.health.app.common.exception;

public class DuplicateEntityException extends BadRequestException {

    public DuplicateEntityException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DuplicateEntityException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
