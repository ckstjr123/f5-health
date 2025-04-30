package f5.health.app.service.auth;

import f5.health.app.constant.OAuth2Provider;
import f5.health.app.entity.Device.Device;
import f5.health.app.entity.Device.DeviceInfo;
import f5.health.app.entity.Member;
import f5.health.app.exception.member.MemberAlreadyJoinedException;
import f5.health.app.jwt.JwtProvider;
import f5.health.app.jwt.vo.JwtResponse;
import f5.health.app.service.auth.client.OAuth2KakaoClient;
import f5.health.app.service.auth.vo.OAuth2LoginRequest;
import f5.health.app.service.auth.vo.SignUpRequest;
import f5.health.app.service.auth.vo.oauth2userinfo.OAuth2UserInfo;
import f5.health.app.service.device.DeviceService;
import f5.health.app.service.member.MemberService;
import f5.health.app.vo.auth.AuthResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static f5.health.app.constant.AuthStatus.*;
import static f5.health.app.constant.OAuth2Provider.KAKAO;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OAuth2KakaoClient oauth2KakaoClient;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final DeviceService deviceService;


    @Transactional
    public AuthResult signin(OAuth2Provider provider, OAuth2LoginRequest loginRequest) {
        OAuth2UserInfo oauth2Userinfo = this.fetchOAuth2UserInfo(provider, loginRequest.getAccessToken());

        return memberService.findByEmail(oauth2Userinfo.getEmail())
                .map(findMember -> AuthResult.of(SIGNIN, this.issueTokensAndRegisterDevice(findMember, loginRequest.getDeviceInfo())))
                .orElse(AuthResult.of(SIGNUP_REQUIRED, null));
    }

    @Transactional
    public AuthResult join(OAuth2Provider provider, SignUpRequest signUpRequest) {
        // 유저 정보 조회 및 회원가입 중복 체크
        OAuth2UserInfo oauth2Userinfo = this.fetchOAuth2UserInfo(provider, signUpRequest.getAccessToken());
        this.validateDuplicateMember(oauth2Userinfo.getEmail());

        Member joinMember = memberService.join(oauth2Userinfo, signUpRequest.getMemberCheckUp()); // 회원가입

        JwtResponse tokenResponse = this.issueTokensAndRegisterDevice(joinMember, signUpRequest.getDeviceInfo());
        return AuthResult.of(JOIN, tokenResponse);
    }


    /** 액세스 토큰으로 사용자 정보 조회하는 API 호출 */
    private OAuth2UserInfo fetchOAuth2UserInfo(OAuth2Provider provider, String accessToken) {
        switch (provider) {
//          case APPLE:
            case KAKAO:
                return this.oauth2KakaoClient.getKakaoUserInfo(KAKAO.accessTokenPrefix() + accessToken);
            default:
                throw new IllegalStateException("Unsupported OAuth2 Provider: " + provider);
        }
    }

    private JwtResponse issueTokensAndRegisterDevice(Member member, DeviceInfo deviceInfo) {
        // access token & refresh token 발행
        Long memberId = member.getId();
        String accessToken = this.jwtProvider.issueAccessToken(memberId, member.getUsername(), member.getRole().name());
        JwtProvider.RefreshToken refreshToken = this.jwtProvider.issueRefreshToken(memberId);

        this.deviceService.register(Device.of(member, deviceInfo, refreshToken)); // 갱신 토큰과 함께 해당 접속 유저 디바이스 등록

        return new JwtResponse(accessToken, refreshToken.getValue()); // 생성된 토큰 응답
    }

    private void validateDuplicateMember(String email) {
        Optional<Member> findMember = this.memberService.findByEmail(email);
        if (!findMember.isEmpty()) {
            throw new MemberAlreadyJoinedException("이미 가입한 회원입니다.");
        }
    }

}
