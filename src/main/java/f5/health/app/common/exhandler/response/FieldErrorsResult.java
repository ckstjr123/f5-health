package f5.health.app.common.exhandler.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Schema(description = "검증에 실패한 요청 필드들 에러 응답")
@Getter
@RequiredArgsConstructor
public class FieldErrorsResult {

    @Schema(description = "검증 실패 필드 리스트")
    private final List<CustomFieldError> fieldErrors;
}
