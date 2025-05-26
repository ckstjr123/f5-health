package f5.health.app.jwt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtMember {

    private Long id;
    private String role;

    public JwtMember(Long id, String role) {
        this.id = id;
        this.role = role;
    }

/*    public static JwtMemberBuilder builder() {
        return new JwtMemberBuilder();
    }

    public static class JwtMemberBuilder {

        private final JwtMember jwtMember;

        private JwtMemberBuilder() {
            this.jwtMember = new JwtMember();
        }

        public JwtMemberBuilder memberId(Long memberId) {
            this.jwtMember.id = memberId;
            return this;
        }

        public JwtMemberBuilder role(String role) {
            this.jwtMember.role = role;
            return this;
        }

        public JwtMember build() {
            return this.jwtMember;
        }
    }*/

}
