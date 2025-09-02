package f5.health.app.auth.service.oauth2client;

import f5.health.app.auth.constant.OAuth2Provider;
import f5.health.app.member.service.oauth2userinfo.OAuth2UserInfo;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OAuth2ClientService {

    private final Map<String, OAuth2Client> oauth2ClientMap;

    public OAuth2ClientService(final Map<String, OAuth2Client> oauth2ClientMap) {
        this.oauth2ClientMap = new ConcurrentHashMap<>(oauth2ClientMap);
    }

    /** 액세스 토큰으로 사용자 정보 조회하는 API 호출 */
    public OAuth2UserInfo fetchOAuth2UserInfo(OAuth2Provider provider, String accessToken) {
        OAuth2Client oauth2Client = oauth2ClientMap.get(provider.oauth2ClientKey());
        if (oauth2Client == null) {
            throw new IllegalStateException("Unsupported OAuth2 Provider: " + provider);
        }

        return oauth2Client.getOAuth2UserInfo(provider.accessTokenPrefix() + accessToken);
    }
}
