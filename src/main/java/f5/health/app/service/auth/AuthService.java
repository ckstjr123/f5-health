package f5.health.app.service.auth;

import f5.health.app.constant.auth.OAuth2Provider;
import f5.health.app.entity.device.Device;
import f5.health.app.entity.member.Member;
import f5.health.app.exception.auth.AccessDeniedException;
import f5.health.app.exception.auth.RefreshViolationException;
import f5.health.app.jwt.JwtProvider;
import f5.health.app.jwt.vo.JwtResponse;
import f5.health.app.service.auth.client.oauth2client.OAuth2ClientService;
import f5.health.app.service.auth.vo.OAuth2LoginRequest;
import f5.health.app.service.auth.vo.SignUpRequest;
import f5.health.app.service.auth.vo.oauth2userinfo.OAuth2UserInfo;
import f5.health.app.service.device.DeviceService;
import f5.health.app.service.member.MemberService;
import f5.health.app.vo.auth.OAuth2LoginResult;
import f5.health.app.vo.device.DeviceAndMemberRole;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static f5.health.app.constant.auth.OAuth2LoginStatus.OAUTH2_LOGIN_SUCCESS;
import static f5.health.app.constant.auth.OAuth2LoginStatus.SIGNUP_REQUIRED;
import static f5.health.app.exception.auth.AuthErrorCode.NOT_MATCH_REFRESH_JWT;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final OAuth2ClientService oauth2ClientService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final DeviceService deviceService;

    public OAuth2LoginResult login(OAuth2Provider provider, OAuth2LoginRequest loginRequest) {
        OAuth2UserInfo oauth2UserInfo = oauth2ClientService.fetchOAuth2UserInfo(provider, loginRequest.getAccessToken());

        return memberService.findByEmail(oauth2UserInfo.getEmail())
                .map(findMember -> {
                    JwtResponse tokenResponse = issueJwtokens(findMember);
                    deviceService.registerDevice(findMember.getId(), loginRequest.getDeviceInfo(), tokenResponse.getRefreshToken());
                    return OAuth2LoginResult.of(OAUTH2_LOGIN_SUCCESS, tokenResponse);
                })
                .orElse(OAuth2LoginResult.of(SIGNUP_REQUIRED, null));
    }

    public JwtResponse join(OAuth2Provider provider, SignUpRequest signUpRequest) {
        // 액세스 토큰을 통해 유저 정보 조회
        OAuth2UserInfo oauth2Userinfo = oauth2ClientService.fetchOAuth2UserInfo(provider, signUpRequest.getAccessToken());

        Long memberId = memberService.join(oauth2Userinfo, signUpRequest.getMemberCheckUp());
        Member joinMember = memberService.findById(memberId);

        JwtResponse tokenResponse = issueJwtokens(joinMember);
        deviceService.registerDevice(joinMember.getId(), signUpRequest.getDeviceInfo(), tokenResponse.getRefreshToken());
        return tokenResponse;
    }

    public JwtResponse refresh(String refreshToken) {
        this.jwtProvider.parseClaims(refreshToken);
        DeviceAndMemberRole deviceAndMemberRole = deviceService.findDeviceAndMemberRoleBy(refreshToken)
                .orElseThrow(() -> new RefreshViolationException(NOT_MATCH_REFRESH_JWT));

        return this.refreshRotation(deviceAndMemberRole);
    }

    public void logout(Long logoutMemberId, String refreshToken) {
        Claims rtClaims = this.jwtProvider.parseClaims(refreshToken);
        if (!logoutMemberId.equals(Long.valueOf(rtClaims.getSubject()))) {
            throw new AccessDeniedException("로그아웃 권한이 없습니다.");
        }

        this.deviceService.deleteByMemberIdAndRefreshToken(logoutMemberId, refreshToken);
    }

    private JwtResponse issueJwtokens(Member member) {
        Long memberId = member.getId();
        String accessToken = jwtProvider.issueAccessToken(memberId, member.getRole().name());
        JwtProvider.RefreshToken refreshToken = jwtProvider.issueRefreshToken(memberId);
        return new JwtResponse(accessToken, refreshToken);
    }

    private JwtResponse refreshRotation(DeviceAndMemberRole deviceAndMemberRole) {
        Device refreshDevice = deviceAndMemberRole.getDevice();
        Long refreshMemberId = refreshDevice.getDeviceId().getMember().getId(); // MEMBER 테이블 조회 X

        String accessToken = jwtProvider.issueAccessToken(refreshMemberId, deviceAndMemberRole.getMemberRole());
        JwtProvider.RefreshToken refreshToken = jwtProvider.issueRefreshToken(refreshMemberId);
        refreshDevice.rotateRefreshToken(refreshToken);

        return new JwtResponse(accessToken, refreshToken);
    }

}
