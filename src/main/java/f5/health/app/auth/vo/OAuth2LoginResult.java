package f5.health.app.auth.vo;

import f5.health.app.auth.constant.OAuth2LoginStatus;
import f5.health.app.auth.jwt.vo.JwtResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Schema(description = "회원에 대한 인증 결과. 클라이언트는 인증 상태를 확인하여 추가 정보를 입력받는 페이지로 이동하거나 로그인 성공 시 응답 바디에 담긴 토큰을 키체인에 저장")
@Getter
public class OAuth2LoginResult {

    @Schema(description = "인증 상태")
    private final OAuth2LoginStatus oauth2LoginStatus;

    @Schema(description = "응답 토큰", nullable = true)
    private final JwtResponse tokenResponse;

    private OAuth2LoginResult(OAuth2LoginStatus signInStatus, JwtResponse tokenResponse) {
        this.oauth2LoginStatus = signInStatus;
        this.tokenResponse = tokenResponse;
    }

    public static OAuth2LoginResult of(OAuth2LoginStatus signInStatus, JwtResponse tokenResponse) {
        return new OAuth2LoginResult(signInStatus, tokenResponse);
    }

    public HttpStatusCode httpStatus() {
        return this.oauth2LoginStatus.httpStatus();
    }
}