package f5.health.app.vo;

import f5.health.app.constant.AuthStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "요청 회원의 현재 인증 상태를 나타냄. 클라이언트는 인증 상태를 확인하여 추가 정보를 입력받는 페이지로 이동하거나 로그인 성공 시 응답 헤더에 담긴 JWT 토큰을 키체인에 저장")
@Getter
@RequiredArgsConstructor
public class AuthResponse {

    @Schema(description = "인증 상태", example = "SIGNUP_REQUIRED")
    private final AuthStatus authStatus;
}