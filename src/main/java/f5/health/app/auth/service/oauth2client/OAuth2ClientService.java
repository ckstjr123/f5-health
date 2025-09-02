package f5.health.app.auth.service.oauth2client;

import f5.health.app.auth.constant.OAuth2Provider;
import f5.health.app.member.service.oauth2userinfo.OAuth2UserInfo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Service
public class OAuth2ClientService {

    private final Map<String, OAuth2Client> oauth2ClientMap;

    @Autowired
    public OAuth2ClientService(List<OAuth2Client> oauth2ClientList) {
        this.oauth2ClientMap = oauth2ClientList.stream()
                .collect(Collectors.toMap(OAuth2Client::getOAuth2ClientName, Function.identity()));
    }

    /** 액세스 토큰으로 사용자 정보 조회하는 API 호출 */
    public OAuth2UserInfo fetchOAuth2UserInfo(OAuth2Provider provider, String accessToken) {
        OAuth2Client oauth2Client = this.oauth2ClientMap.get(provider.getOAuth2ClientName());
        if (oauth2Client == null) {
            throw new IllegalStateException("Unsupported OAuth2 provider: " + provider);
        }

        return oauth2Client.getOAuth2UserInfo(provider.accessTokenPrefix() + accessToken);
    }
}
