package f5.health.app.service.auth.client.oauth2client;

import f5.health.app.service.auth.vo.oauth2userinfo.KakaoUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static f5.health.app.service.auth.client.oauth2client.OAuth2KakaoClient.KAKA0_OAUTH2_CLIENT_NAME;

@FeignClient(name = KAKA0_OAUTH2_CLIENT_NAME, url = "https://kapi.kakao.com")
public interface OAuth2KakaoClient extends OAuth2Client {

    String KAKA0_OAUTH2_CLIENT_NAME = "oauth2KakaoClient";

    @Override
    default String getOAuth2ClientName() {
        return KAKA0_OAUTH2_CLIENT_NAME;
    }

    @Override
    @PostMapping("/v2/user/me")
    KakaoUserInfo getOAuth2UserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String kakaoAccessToken);
}
