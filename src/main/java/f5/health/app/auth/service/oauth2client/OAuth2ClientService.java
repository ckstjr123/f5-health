package f5.health.app.auth.service.oauth2client;

import f5.health.app.auth.constant.OAuth2Provider;
import f5.health.app.member.service.oauth2userinfo.OAuth2UserInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OAuth2ClientService {

    private final Map<String, OAuth2Client> oauth2Clients;

    public OAuth2ClientService(final Map<String, OAuth2Client> oauth2Clients) {
        this.oauth2Clients = Map.copyOf(oauth2Clients);
    }

    /** 액세스 토큰으로 사용자 정보 조회하는 API 호출 */
    public OAuth2UserInfo loadOAuth2UserInfo(OAuth2Provider provider, String accessToken) {
        OAuth2Client oauth2Client = oauth2Clients.get(provider.oauth2ClientKey());
        if (oauth2Client == null) {
            throw new IllegalStateException("Unsupported OAuth2 Provider: " + provider);
        }

        return oauth2Client.fetchOAuth2UserInfo(provider.accessTokenPrefix() + accessToken);
    }
}
