package f5.health.app.auth.service;

import f5.health.app.auth.constant.OAuth2Provider;
import f5.health.app.auth.exception.AuthenticationException;
import f5.health.app.auth.exception.RefreshViolationException;
import f5.health.app.auth.jwt.JwtProvider;
import f5.health.app.auth.jwt.vo.JwtResponse;
import f5.health.app.auth.service.oauth2client.OAuth2ClientService;
import f5.health.app.auth.service.vo.request.OAuth2LoginRequest;
import f5.health.app.auth.service.vo.request.SignUpRequest;
import f5.health.app.auth.vo.OAuth2LoginResult;
import f5.health.app.common.RedisManager;
import f5.health.app.member.constant.Role;
import f5.health.app.member.entity.Member;
import f5.health.app.member.service.MemberService;
import f5.health.app.member.service.oauth2userinfo.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static f5.health.app.auth.constant.OAuth2LoginStatus.CHECKUP_REQUIRED;
import static f5.health.app.auth.constant.OAuth2LoginStatus.OAUTH2_LOGIN_SUCCESS;
import static f5.health.app.auth.exception.AuthErrorCode.EXPIRED_JWT;
import static f5.health.app.auth.exception.AuthErrorCode.NOT_MATCH_REFRESH_JWT;
import static f5.health.app.auth.jwt.JwtProvider.REFRESH_TOKEN_EXPIRATION_SEC;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final OAuth2ClientService oauth2ClientService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final RedisManager redisManager;
    private final String REFRESH_TOKEN_PREFIX = "refresh_token:member:";

    public OAuth2LoginResult login(OAuth2Provider provider, OAuth2LoginRequest loginRequest) {
        OAuth2UserInfo oauth2UserInfo = oauth2ClientService.fetchOAuth2UserInfo(provider, loginRequest.accessToken());

        return memberService.findByEmail(oauth2UserInfo.getEmail())
                .map(findMember -> OAuth2LoginResult.of(OAUTH2_LOGIN_SUCCESS, issueJWTokens(findMember)))
                .orElse(OAuth2LoginResult.of(CHECKUP_REQUIRED, null));
    }

    public JwtResponse join(OAuth2Provider provider, SignUpRequest signUpRequest) {
        OAuth2UserInfo oauth2UserInfo = oauth2ClientService.fetchOAuth2UserInfo(provider, signUpRequest.accessToken());

        Long memberId = memberService.join(oauth2UserInfo, signUpRequest.memberCheckUp());

        return issueJWTokens(memberService.findById(memberId));
    }

    public JwtResponse refresh(String refreshToken) {
        Long memberId = Long.valueOf(jwtProvider.parseClaims(refreshToken).getSubject());

        String savedRefreshToken = redisManager.get(REFRESH_TOKEN_PREFIX + memberId);
        if (savedRefreshToken == null) {
            throw new AuthenticationException(EXPIRED_JWT);
        }
        if (!Objects.equals(refreshToken, savedRefreshToken)) {
            throw new RefreshViolationException(NOT_MATCH_REFRESH_JWT);
        }

        return issueJWTokens(memberService.findById(memberId));
    }

    private JwtResponse issueJWTokens(Member member) {
        Long memberId = member.getId();
        String accessToken = jwtProvider.issueAccessToken(memberId, member.getRole().name());
        String refreshToken = jwtProvider.issueRefreshToken(memberId);
        redisManager.set(REFRESH_TOKEN_PREFIX + memberId, refreshToken, REFRESH_TOKEN_EXPIRATION_SEC); //
        return new JwtResponse(accessToken, refreshToken);
    }
}
