package f5.health.app.exception.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "요청 필드 검증 에러")
@Getter
public class CustomFieldError {

    @Schema(description = "검증 실패 필드", example = "gender")
    private final String field;

    @Schema(description = "해당 검증 실패 필드 에러 메시지", example = "성별이 잘못 입력되었습니다.")
    private final String message;

    public CustomFieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "field='" + this.field + '\'' +
                ", message='" + this.message + '\'' +
                '}';
    }
}
