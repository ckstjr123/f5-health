package f5.health.app.jwt.vo;

import f5.health.app.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "토큰 응답")
@Getter //Swagger
public final class JwtResponse {

    @Schema(description = "인증 토큰 타입")
    private final String accessTokenType;

    @Schema(description = "인증 토큰")
    private final String accessToken;

    @Schema(description = "갱신 토큰")
    private final String refreshToken;

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessTokenType = JwtProvider.ACCESS_TOKEN_TYPE;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
