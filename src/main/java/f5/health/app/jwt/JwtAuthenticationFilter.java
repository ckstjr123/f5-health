package f5.health.app.jwt;

import f5.health.app.exception.auth.AuthenticationException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static f5.health.app.exception.auth.AuthErrorCode.INVALID_TOKEN_TYPE;
import static f5.health.app.jwt.JwtProvider.ACCESS_TOKEN_TYPE;

/**
 * JWT 인증 필터(요청에 대해서 단 한번만 호출되는 OncePerRequestFilter 상속)
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //    private final String[] exceptURIs = {"/signin", "/signup", "/refresh"};
    private final String ACCESS_TOKEN_PREFIX = ACCESS_TOKEN_TYPE + " ";
    private final JwtProvider jwtProvider;

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return PatternMatchUtils.simpleMatch(this.exceptURIs, request.getRequestURI());
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = this.resolveAccessToken(request.getHeader(HttpHeaders.AUTHORIZATION));

            Claims accessTokenClaims = jwtProvider.parseClaims(accessToken);
            JwtUser loginMember = JwtUser.from(accessTokenClaims); // 임시 세션 보관용 유저 객체 생성

            Authentication authentication = new UsernamePasswordAuthenticationToken(loginMember, null, List.of(new SimpleGrantedAuthority(loginMember.getRole())));
            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증된 유저에 대해 현재 요청 동안에만 사용될 임시 세션
            log.info("Access token authentication success, memberId={}", loginMember.getMemberId());
        } catch (AuthenticationException ex) {
            // AuthenticationEntryPoint에서 처리
            request.setAttribute(JwtProvider.JWT_EXCEPTION_ATTRIBUTE, ex.getErrorCode());
            throw ex;
        }

        filterChain.doFilter(request, response); //
    }

    private String resolveAccessToken(String authHeader) {
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(ACCESS_TOKEN_PREFIX)) {
            throw new AuthenticationException(INVALID_TOKEN_TYPE);
        }

        return authHeader.replace(ACCESS_TOKEN_PREFIX, "");
    }

}