package f5.health.app.jwt.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "토큰 갱신 요청 VO")
@Getter
@RequiredArgsConstructor
public class JwtReissueRequest {

    @Schema(description = "만료된 인증 토큰")
    @NotBlank
    private final String expiredAccessToken;

    @Schema(description = "갱신 토큰")
    @NotBlank
    private final String refreshToken;
}