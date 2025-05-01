package f5.health.app.jwt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class JwtUser {

    private Long memberId;
    private String role;

    public JwtUser(Long memberId, String role) {
        this.memberId = memberId;
        this.role = role;
    }

//    public static JwtUserBuilder builder() {
//        return new JwtUserBuilder();
//    }

/*    public static class JwtUserBuilder {

        private final JwtUser jwtUser;

        private JwtUserBuilder() {
            this.jwtUser = new JwtUser();
        }

        public JwtUserBuilder memberId(Long memberId) {
            this.jwtUser.memberId = memberId;
            return this;
        }

        public JwtUserBuilder role(String role) {
            this.jwtUser.role = role;
            return this;
        }

        public JwtUser build() {
            return this.jwtUser;
        }
    }*/

}
