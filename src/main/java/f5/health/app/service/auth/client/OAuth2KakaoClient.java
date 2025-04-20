package f5.health.app.service.auth.client;

import f5.health.app.service.auth.vo.oauth2userinfo.KakaoUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "oauth2KakaoClient", url = "https://kapi.kakao.com")
public interface OAuth2KakaoClient {

    @PostMapping("/v2/user/me")
    KakaoUserInfo getKakaoUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String kakaoAccessToken);
}
