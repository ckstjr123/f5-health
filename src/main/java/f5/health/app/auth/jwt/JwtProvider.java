package f5.health.app.auth.jwt;

import com.fasterxml.jackson.annotation.JsonValue;
import f5.health.app.auth.exception.AuthErrorCode;
import f5.health.app.auth.exception.AuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;

import static f5.health.app.auth.jwt.constant.JwtConst.ROLE;

@Slf4j
@Component
public class JwtProvider {

    public static final String ACCESS_TOKEN_TYPE = "Bearer";
    private final long ACCESS_TOKEN_EXPIRE_MS = Duration.ofMinutes(30).toMillis();
    private final SecretKey secretKey;

    public JwtProvider(@Value("${spring.jwt.secret}") String secret) {
        // HS256: 양방향 대칭키 암호화 알고리즘
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }


    public String issueAccessToken(Long memberId, String role) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_MS);
        return Jwts.builder()
                .subject(memberId.toString())
                .claim(ROLE, role)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(this.secretKey)
                .compact();
    }

    public RefreshToken issueRefreshToken(Long memberId) {
        return this.new RefreshToken(memberId);
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(this.secretKey).build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException ex) {
            throw new AuthenticationException(AuthErrorCode.SIGNATURE_JWT, ex);
        } catch (MalformedJwtException ex) {
            throw new AuthenticationException(AuthErrorCode.MALFORMED_JWT, ex);
        } catch (ExpiredJwtException ex) {
            throw new AuthenticationException(AuthErrorCode.EXPIRED_JWT, ex);
        } catch (UnsupportedJwtException ex) {
            throw new AuthenticationException(AuthErrorCode.UNSUPPORTED_JWT, ex);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new AuthenticationException(AuthErrorCode.INVALID_JWT, ex);
        }
    }

    public final class RefreshToken {

        private static final long REFRESH_TOKEN_EXPIRE_MS = Duration.ofDays(7).toMillis();
        private final String value;
        private final Date expiration;

        private RefreshToken(Long memberId) {
            Date now = new Date();
            Date expirationDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_MS);
            this.expiration = expirationDate;
            this.value = Jwts.builder()
                    .subject(memberId.toString())
                    .issuedAt(now)
                    .expiration(expirationDate)
                    .signWith(secretKey)
                    .compact();
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        public Date getExpiration() {
            return this.expiration;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RefreshToken that)) return false;
            return Objects.equals(this.value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.value);
        }
    }

}