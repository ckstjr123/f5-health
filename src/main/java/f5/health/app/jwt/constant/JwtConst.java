package f5.health.app.jwt.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtConst {
    public static final String ROLE = "role";
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    public static final String JWT_EXCEPTION_ATTRIBUTE = "JWT_EXCEPTION";
}