package f5.health.app.auth.service;

import f5.health.app.auth.constant.OAuth2Provider;
import f5.health.app.session.entity.Session;
import f5.health.app.member.entity.Member;
import f5.health.app.auth.exception.RefreshViolationException;
import f5.health.app.auth.jwt.JwtProvider;
import f5.health.app.auth.jwt.vo.JwtResponse;
import f5.health.app.auth.service.oauth2client.OAuth2ClientService;
import f5.health.app.auth.service.vo.request.OAuth2LoginRequest;
import f5.health.app.auth.service.vo.request.SignUpRequest;
import f5.health.app.member.service.oauth2userinfo.OAuth2UserInfo;
import f5.health.app.session.service.SessionService;
import f5.health.app.member.service.MemberService;
import f5.health.app.auth.vo.OAuth2LoginResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static f5.health.app.auth.constant.OAuth2LoginStatus.OAUTH2_LOGIN_SUCCESS;
import static f5.health.app.auth.constant.OAuth2LoginStatus.CHECKUP_REQUIRED;
import static f5.health.app.auth.exception.AuthErrorCode.NOT_MATCH_REFRESH_JWT;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final OAuth2ClientService oauth2ClientService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final SessionService sessionService;

    public OAuth2LoginResult login(OAuth2Provider provider, OAuth2LoginRequest loginRequest) {
        OAuth2UserInfo oauth2UserInfo = oauth2ClientService.fetchOAuth2UserInfo(provider, loginRequest.accessToken());

        return memberService.findByEmail(oauth2UserInfo.getEmail())
                .map(findMember -> {
                    JwtResponse tokenResponse = issueJWTokens(findMember);
                    sessionService.save(findMember.getId(), loginRequest.deviceInfo(), tokenResponse.refreshToken());
                    return OAuth2LoginResult.of(OAUTH2_LOGIN_SUCCESS, tokenResponse);
                })
                .orElse(OAuth2LoginResult.of(CHECKUP_REQUIRED, null));
    }

    public JwtResponse join(OAuth2Provider provider, SignUpRequest signUpRequest) {
        OAuth2UserInfo oauth2UserInfo = oauth2ClientService.fetchOAuth2UserInfo(provider, signUpRequest.accessToken());

        Long memberId = memberService.join(oauth2UserInfo, signUpRequest.memberCheckUp());
        Member joinMember = memberService.findById(memberId);

        JwtResponse tokenResponse = issueJWTokens(joinMember);
        sessionService.save(joinMember.getId(), signUpRequest.deviceInfo(), tokenResponse.refreshToken());
        return tokenResponse;
    }

    public JwtResponse refresh(String refreshToken) {
        jwtProvider.parseClaims(refreshToken);
        Session session = sessionService.findByRefreshTokenJoinFetch(refreshToken)
                .orElseThrow(() -> new RefreshViolationException(NOT_MATCH_REFRESH_JWT));

        JwtResponse tokenResponse = issueJWTokens(session.getMember());
        session.rotateRefreshToken(tokenResponse.refreshToken().value(), tokenResponse.refreshToken().getExpiration());
        return tokenResponse;
    }


    private JwtResponse issueJWTokens(Member member) {
        Long memberId = member.getId();
        String accessToken = jwtProvider.issueAccessToken(memberId, member.getRole().name());
        JwtProvider.RefreshToken refreshToken = jwtProvider.issueRefreshToken(memberId);
        return new JwtResponse(accessToken, refreshToken);
    }

}
