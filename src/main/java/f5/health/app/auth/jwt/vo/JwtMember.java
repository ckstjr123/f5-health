package f5.health.app.auth.jwt.vo;

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
}
