package f5.health.app.auth.jwt.vo;

import f5.health.app.auth.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 응답")
public record JwtResponse(@Schema(description = "인증 토큰") String accessToken,
                          @Schema(description = "갱신 토큰") JwtProvider.RefreshToken refreshToken) {
}
