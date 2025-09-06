package f5.health.app.auth.jwt;

import f5.health.app.auth.exception.AuthenticationException;
import f5.health.app.auth.jwt.vo.JwtMember;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static f5.health.app.common.config.SecurityConfig.AUTH_EXCLUDE_URIS;
import static f5.health.app.auth.jwt.constant.JwtConst.JWT_EXCEPTION_ATTRIBUTE;
import static f5.health.app.auth.jwt.constant.JwtConst.ROLE;
import static f5.health.app.auth.jwt.JwtProvider.ACCESS_TOKEN_TYPE;

/**
 * JWT 인증 필터(요청에 대해서 단 한번만 호출되는 OncePerRequestFilter 상속)
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String ACCESS_TOKEN_PREFIX = ACCESS_TOKEN_TYPE + " ";
    private final JwtProvider jwtProvider;
    private final AntPathMatcher pathMatcher;

    @Autowired
    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return Arrays.stream(AUTH_EXCLUDE_URIS).anyMatch(excludeURI -> pathMatcher.match(excludeURI, requestURI));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = this.resolveAccessToken(request.getHeader(HttpHeaders.AUTHORIZATION));
            Claims atClaims = jwtProvider.parseClaims(accessToken);

            // 임시 세션 보관용 유저 객체 생성
            JwtMember loginMember = new JwtMember(Long.valueOf(atClaims.getSubject()), atClaims.get(ROLE, String.class));
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginMember, null, List.of(new SimpleGrantedAuthority(loginMember.getRole())));
            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증된 유저에 대해 현재 요청 동안에만 사용될 임시 세션
        } catch (AuthenticationException ex) {
            request.setAttribute(JWT_EXCEPTION_ATTRIBUTE, ex.getErrorCode()); // AuthenticationEntryPoint에서 처리
            throw ex;
        }

        filterChain.doFilter(request, response); //
    }

    private String resolveAccessToken(String authHeader) {
        return StringUtils.hasText(authHeader) && authHeader.startsWith(ACCESS_TOKEN_PREFIX) ?
                authHeader.replace(ACCESS_TOKEN_PREFIX, "") : "";
    }
}