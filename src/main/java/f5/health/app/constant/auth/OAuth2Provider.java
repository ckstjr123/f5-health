package f5.health.app.constant.auth;

import static f5.health.app.jwt.JwtProvider.ACCESS_TOKEN_TYPE;

public enum OAuth2Provider {
    KAKAO(ACCESS_TOKEN_TYPE + " ");
//    , APPLE;

    private final String ACCESS_TOKEN_PREFIX;

    OAuth2Provider(String accessTokenPrefix) {
        this.ACCESS_TOKEN_PREFIX = accessTokenPrefix;
    }


    public String accessTokenPrefix() {
        return this.ACCESS_TOKEN_PREFIX;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
