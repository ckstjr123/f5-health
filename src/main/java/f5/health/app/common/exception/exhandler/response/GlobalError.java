package f5.health.app.common.exception.exhandler.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

@Schema(description = "요청 전역 검증 실패로 인한 에러")
@Getter
@ToString
public class GlobalError {

    @Schema(description = "코드명", example = "MenuSize")
    private final String code;

    @Schema(description = "검증 실패 전역 메시지", example = "메뉴를 입력해 주세요.")
    private final String message;

    public GlobalError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
