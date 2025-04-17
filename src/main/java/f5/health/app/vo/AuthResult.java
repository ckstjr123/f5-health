package f5.health.app.vo;

import f5.health.app.constant.AuthStatus;
import f5.health.app.jwt.vo.JwtResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Schema(description = "회원에 대한 인증 결과. 클라이언트는 인증 상태를 확인하여 추가 정보를 입력받는 페이지로 이동하거나 로그인 성공 시 응답 바디에 담긴 토큰을 키체인에 저장")
@Getter
@RequiredArgsConstructor
public class AuthResult {

    @Schema(description = "인증 상태", example = "SIGNUP_REQUIRED")
    private final AuthStatus authStatus;

    @Schema(description = "응답 토큰", nullable = true)
    private final JwtResponse tokenResponse;


    public HttpStatusCode httpStatus() {
        return this.getAuthStatus().httpStatus();
    }
}