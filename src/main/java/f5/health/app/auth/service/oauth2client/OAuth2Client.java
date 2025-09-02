package f5.health.app.auth.service.oauth2client;

import f5.health.app.member.service.oauth2userinfo.OAuth2UserInfo;

public interface OAuth2Client {
    String getOAuth2ClientName();
    OAuth2UserInfo getOAuth2UserInfo(String accessToken);
}
