package f5.health.app.auth.constant;

import f5.health.app.auth.service.oauth2client.OAuth2KakaoClient;

import static f5.health.app.auth.jwt.JwtProvider.ACCESS_TOKEN_TYPE;

public enum OAuth2Provider {

    KAKAO(OAuth2KakaoClient.class.getName(), ACCESS_TOKEN_TYPE + " ");

    private final String oauth2ClientKey;
    private final String accessTokenPrefix;

    OAuth2Provider(String oauth2ClientKey, String accessTokenPrefix) {
        this.oauth2ClientKey = oauth2ClientKey;
        this.accessTokenPrefix = accessTokenPrefix;
    }

    public String oauth2ClientKey() {
        return this.oauth2ClientKey;
    }

    public String accessTokenPrefix() {
        return this.accessTokenPrefix;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
