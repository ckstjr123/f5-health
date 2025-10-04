package f5.health.app.auth.service.oauth2client;

import f5.health.app.member.service.oauth2userinfo.OAuth2UserInfo;

public interface OAuth2Client {
    OAuth2UserInfo fetchOAuth2UserInfo(String accessToken);
}
