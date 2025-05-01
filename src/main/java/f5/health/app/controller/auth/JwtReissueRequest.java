package f5.health.app.controller.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "토큰 재발급 요청 VO")
@Getter
@RequiredArgsConstructor
public class JwtReissueRequest {

    @Schema(description = "갱신 토큰")
    @NotBlank
    private final String refreshToken;
}