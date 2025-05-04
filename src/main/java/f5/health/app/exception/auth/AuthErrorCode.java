package f5.health.app.exception.auth;

import f5.health.app.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum AuthErrorCode implements ErrorCode {

    //JWT 인증 관련 에러 코드
    SIGNATURE_JWT("시그니처 검증에 실패한 토큰입니다."),
    EXPIRED_JWT("만료된 토큰입니다."),
    MALFORMED_JWT("손상된 토큰입니다."),
    UNSUPPORTED_JWT("지원되지 않는 토큰입니다."),
    INVALID_JWT("토큰이 유효하지 않습니다."),
    NOT_MATCH_REFRESH_JWT("갱신 토큰이 일치하지 않습니다."),

    //인가 관련 에러 코드
    FORBIDDEN_REQUEST("허용되지 않은 요청입니다.");

    private final String code;
    private final String message;

    AuthErrorCode(String message) {
        this.code = this.name();
        this.message = message;
    }
}
