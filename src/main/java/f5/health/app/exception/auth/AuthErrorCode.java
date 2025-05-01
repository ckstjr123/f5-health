package f5.health.app.exception.auth;

import f5.health.app.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum AuthErrorCode implements ErrorCode {

    //JWT 인증 관련 예외 타입
    SIGNATURE_JWT("시그니처 검증에 실패한 JWT 토큰입니다."),
    EXPIRED_JWT("만료된 JWT 토큰입니다."),
    MALFORMED_JWT("손상된 토큰입니다."),
    UNSUPPORTED_JWT("지원하지 않는 JWT 토큰입니다."),
    INVALID_JWT("유효하지 않은 JWT 토큰입니다."),

    INVALID_TOKEN_TYPE("유효하지 않은 토큰 인증 방식입니다.");

    private final String code;
    private final String message;

    AuthErrorCode(String message) {
        this.code = this.name();
        this.message = message;
    }
}
