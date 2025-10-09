package f5.health.app.common.exception.exhandler.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Schema(description = "검증 실패 에러 응답")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorsResult {
    
    @Schema(description = "전역 검증 실패 에러")
    private final List<GlobalError> globalErrors;

    @Schema(description = "필드 검증 실패 에러")
    private final List<FieldError> fieldErrors;

    public static ErrorsResult of(List<GlobalError> globalErrors, List<FieldError> fieldErrors) {
        return new ErrorsResult(globalErrors, fieldErrors);
    }
}
