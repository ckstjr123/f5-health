package f5.health.app.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static f5.health.app.jwt.JwtCustomClaimNames.NICKNAME;
import static f5.health.app.jwt.JwtCustomClaimNames.ROLES;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUser {

    private Long memberId;
    private String username;
    private String role;

    public static JwtUserBuilder builder() {
        return new JwtUserBuilder();
    }

    public static JwtUser from(Claims claims) {
        Assert.notEmpty(claims, "claims cannot be empty");
        return JwtUser.builder()
                .memberId(Long.valueOf(claims.getSubject()))
                .nickname(claims.get(NICKNAME, String.class))
                .roles(claims.get(ROLES, String.class))
                .build();
    }

    public static class JwtUserBuilder {

        private final JwtUser jwtUser;

        private JwtUserBuilder() {
            this.jwtUser = new JwtUser();
        }

        public JwtUserBuilder memberId(Long memberId) {
            this.jwtUser.memberId = memberId;
            return this;
        }

        public JwtUserBuilder nickname(String username) {
            this.jwtUser.username = username;
            return this;
        }

        public JwtUserBuilder roles(String role) {
            this.jwtUser.role = role;
            return this;
        }
        public JwtUser build() {
            return this.jwtUser;
        }

    }

}
