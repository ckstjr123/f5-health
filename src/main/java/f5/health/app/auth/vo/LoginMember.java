package f5.health.app.auth.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
public final class LoginMember {

    private final Long id;
    private final String role;

    public LoginMember(Long id, String role) {
        this.id = id;
        this.role = role;
    }
}
