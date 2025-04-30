package f5.health.app.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtCustomClaimNames {
    public static final String NICKNAME = "nickname";
    public static final String ROLES = "roles";
    public static final String TOKEN_USE = "use";
}