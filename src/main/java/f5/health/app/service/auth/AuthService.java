package f5.health.app.service.auth;

import f5.health.app.constant.auth.OAuth2Provider;
import f5.health.app.vo.device.DeviceAndMemberRole;
import f5.health.app.entity.Device.Device;
import f5.health.app.entity.Member;
import f5.health.app.exception.auth.AccessDeniedException;
import f5.health.app.exception.auth.RefreshViolationException;
import f5.health.app.jwt.JwtProvider;
import f5.health.app.jwt.vo.JwtResponse;
import f5.health.app.service.auth.client.OAuth2KakaoClient;
import f5.health.app.service.auth.vo.DeviceInfo;
import f5.health.app.service.auth.vo.OAuth2LoginRequest;
import f5.health.app.service.auth.vo.SignUpRequest;
import f5.health.app.service.auth.vo.oauth2userinfo.OAuth2UserInfo;
import f5.health.app.service.device.DeviceService;
import f5.health.app.service.member.MemberService;
import f5.health.app.vo.auth.OAuth2LoginResult;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static f5.health.app.constant.auth.OAuth2LoginStatus.OAUTH2_LOGIN_SUCCESS;
import static f5.health.app.constant.auth.OAuth2LoginStatus.SIGNUP_REQUIRED;
import static f5.health.app.constant.auth.OAuth2Provider.KAKAO;
import static f5.health.app.exception.auth.AuthErrorCode.NOT_MATCH_REFRESH_JWT;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OAuth2KakaoClient oauth2KakaoClient;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final DeviceService deviceService;


    @Transactional
    public OAuth2LoginResult login(OAuth2Provider provider, OAuth2LoginRequest loginRequest) {
        OAuth2UserInfo oauth2Userinfo = this.fetchOAuth2UserInfo(provider, loginRequest.getAccessToken());

        return memberService.findByEmail(oauth2Userinfo.getEmail())
                .map(findMember -> OAuth2LoginResult.of(OAUTH2_LOGIN_SUCCESS, this.issueTokensAndRegisterDevice(findMember, loginRequest.getDeviceInfo())))
                .orElse(OAuth2LoginResult.of(SIGNUP_REQUIRED, null));
    }

    @Transactional
    public JwtResponse join(OAuth2Provider provider, SignUpRequest signUpRequest) {
        // 액세스 토큰을 통해 유저 정보 조회
        OAuth2UserInfo oauth2Userinfo = this.fetchOAuth2UserInfo(provider, signUpRequest.getAccessToken());

        Member joinMember = memberService.join(oauth2Userinfo, signUpRequest.getMemberCheckUp());

        return this.issueTokensAndRegisterDevice(joinMember, signUpRequest.getDeviceInfo());
    }

    @Transactional
    public JwtResponse refresh(String refreshToken) {
        this.jwtProvider.parseClaims(refreshToken);

        DeviceAndMemberRole deviceAndMemberRole = deviceService.findDeviceAndMemberRoleBy(refreshToken)
                .orElseThrow(() -> new RefreshViolationException(NOT_MATCH_REFRESH_JWT));

        return this.refreshRotation(deviceAndMemberRole);
    }

    @Transactional
    public void logout(Long logoutMemberId, String refreshToken) {
        Claims rtClaims = this.jwtProvider.parseClaims(refreshToken);
        if (!logoutMemberId.equals(Long.valueOf(rtClaims.getSubject()))) {
            throw new AccessDeniedException("로그아웃 권한이 없습니다.");
        }

        this.deviceService.deleteByMemberIdAndRefreshToken(logoutMemberId, refreshToken);
    }


    /** 액세스 토큰으로 사용자 정보 조회하는 API 호출 */
    private OAuth2UserInfo fetchOAuth2UserInfo(OAuth2Provider provider, String accessToken) {
        switch (provider) {
//          case APPLE:
            case KAKAO:
                return oauth2KakaoClient.getKakaoUserInfo(KAKAO.accessTokenPrefix() + accessToken);
            default:
                throw new IllegalStateException("Unsupported OAuth2 provider: " + provider);
        }
    }

    private JwtResponse issueTokensAndRegisterDevice(Member member, DeviceInfo deviceInfo) {
        Long memberId = member.getId();
        String accessToken = jwtProvider.issueAccessToken(memberId, member.getRole().name());
        JwtProvider.RefreshToken refreshToken = jwtProvider.issueRefreshToken(memberId);

        this.deviceService.register(Device.of(member, deviceInfo.getUdid(), deviceInfo.getOs(), refreshToken)); // 갱신 토큰과 함께 해당 접속 유저 디바이스 등록
        return new JwtResponse(accessToken, refreshToken.value());
    }

    private JwtResponse refreshRotation(DeviceAndMemberRole deviceAndMemberRole) {
        Device refreshDevice = deviceAndMemberRole.getDevice();
        Long refreshMemberId = refreshDevice.getDeviceId().getMember().getId(); // MEMBER 테이블 조회 X

        String accessToken = jwtProvider.issueAccessToken(refreshMemberId, deviceAndMemberRole.getMemberRole());
        JwtProvider.RefreshToken refreshToken = jwtProvider.issueRefreshToken(refreshMemberId);
        refreshDevice.rotateRefreshToken(refreshToken);

        return new JwtResponse(accessToken, refreshToken.value());
    }

}
