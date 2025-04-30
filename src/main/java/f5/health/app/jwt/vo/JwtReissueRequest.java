package f5.health.app.jwt.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtReissueRequest {
    @NotBlank
    private final String DeviceUdid;
    @NotBlank
    private final String refreshToken;
}