package f5.health.app.common.exception.exhandler.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

@Schema(description = "요청 필드 검증 에러")
@Getter
@ToString
public class FieldError {

    @Schema(description = "검증 실패 필드", example = "memberCheckUp.birthDate")
    private final String field;

    @Schema(description = "해당 검증 실패 필드 에러 메시지", example = "생년월일을 입력해주세요.")
    private final String message;

    public FieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
