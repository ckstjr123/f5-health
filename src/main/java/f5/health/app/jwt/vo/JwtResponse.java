package f5.health.app.jwt.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "토큰 응답")
@Getter //swagger
@RequiredArgsConstructor
public final class JwtResponse {

    @Schema(description = "인증 토큰")
    private final String accessToken;

    @Schema(description = "갱신 토큰")
    private final String refreshToken;
}

