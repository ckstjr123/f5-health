package f5.health.app.auth.service.oauth2client;

import f5.health.app.auth.constant.OAuth2Provider;
import f5.health.app.member.service.oauth2userinfo.OAuth2UserInfo;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OAuth2ClientService {

    private final Map<String, OAuth2Client> oAuth2Clients;

    public OAuth2ClientService(final Map<String, OAuth2Client> oAuth2Clients) {
        this.oAuth2Clients = new ConcurrentHashMap<>(oAuth2Clients);
    }

    /** 액세스 토큰으로 사용자 정보 조회하는 API 호출 */
    public OAuth2UserInfo fetchOAuth2UserInfo(OAuth2Provider provider, String accessToken) {
        OAuth2Client oAuth2Client = oAuth2Clients.get(provider.oauth2ClientKey());
        if (oAuth2Client == null) {
            throw new IllegalStateException("Unsupported OAuth2 Provider: " + provider);
        }

        return oAuth2Client.getOAuth2UserInfo(provider.accessTokenPrefix() + accessToken);
    }
}
