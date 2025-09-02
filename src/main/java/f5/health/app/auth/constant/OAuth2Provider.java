package f5.health.app.auth.constant;

import f5.health.app.auth.service.oauth2client.OAuth2KakaoClient;

import static f5.health.app.jwt.JwtProvider.ACCESS_TOKEN_TYPE;

public enum OAuth2Provider {

    KAKAO(OAuth2KakaoClient.KAKA0_OAUTH2_CLIENT_NAME, ACCESS_TOKEN_TYPE + " ");
//    , APPLE;

    private final String oauth2ClientName;
    private final String accessTokenPrefix;

    OAuth2Provider(String oauth2ClientName, String accessTokenPrefix) {
        this.oauth2ClientName = oauth2ClientName;
        this.accessTokenPrefix = accessTokenPrefix;
    }

    public String getOAuth2ClientName() {
        return this.oauth2ClientName;
    }

    public String accessTokenPrefix() {
        return this.accessTokenPrefix;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
