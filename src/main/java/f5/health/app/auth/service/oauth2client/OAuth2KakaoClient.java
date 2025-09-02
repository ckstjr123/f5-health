package f5.health.app.auth.service.oauth2client;

import f5.health.app.member.service.oauth2userinfo.KakaoUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "oauth2KakaoClient", url = "https://kapi.kakao.com")
public interface OAuth2KakaoClient extends OAuth2Client {

    @Override
    @PostMapping("/v2/user/me")
    KakaoUserInfo getOAuth2UserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String kakaoAccessToken);
}
