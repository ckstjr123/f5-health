package f5.health.app.jwt.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class JwtResponse {
    private final String accessToken;
    private final String refreshToken;
}

