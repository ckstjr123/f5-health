package f5.health.app.service.auth.vo.oauth2userinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [액세스 토큰 전송 후 응답받은 OAuth2 카카오 로그인 사용자 정보]
 * HTTP/1.1 200 OK
 * {
 * "id":123456789,
 * "connected_at": "2022-04-11T01:45:28Z",
 * "kakao_account": {
 * // 프로필 또는 닉네임 동의항목 필요
 * "profile_nickname_needs_agreement	": false,
 * "profile": {
 * // 프로필 또는 닉네임 동의항목 필요
 * "nickname": "홍길동",
 * },
 * // 카카오계정(이메일) 동의항목 필요
 * "email_needs_agreement":false,
 * "is_email_valid": true,
 * "is_email_verified": true,
 * "email": "sample@sample.com",
 * }
 * }
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoUserInfo implements OAuth2UserInfo {

    private String id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Override
    public String getOAuthId() {
        return this.id;
    }

    @Override
    public String getEmail() {
        return this.kakaoAccount.email;
    }

    @Override
    public String getNickname() {
        return this.kakaoAccount.profile.nickname;
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class KakaoAccount {

        private Profile profile;
        private String email;

        @Getter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        static class Profile {
            private String nickname;
        }
    }
}
