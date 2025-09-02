package f5.health.app.member.service.oauth2userinfo;


public interface OAuth2UserInfo {

    String getOAuthId();

    String getEmail();

    String getNickname();
}
