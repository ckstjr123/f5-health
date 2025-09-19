package f5.health.app.common.exception.exhandler.response;

import f5.health.app.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "예외 응답")
@Getter
public class ExceptionResult {

    @Schema(description = "에러 코드", example = "EXPIRED_JWT")
    private final String errorCode;
    
    @Schema(description = "예외 메시지", example = "만료된 JWT 토큰입니다.")
    private final String message;

    private ExceptionResult(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ExceptionResult from(final ErrorCode errorCode) {
        return new ExceptionResult(errorCode.getCode(), errorCode.getMessage());
    }

    public static ExceptionResult of(final ErrorCode errorCode, final String message) {
        return new ExceptionResult(errorCode.getCode(), message);
    }
}
