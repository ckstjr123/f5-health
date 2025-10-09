package f5.health.app.auth.vo;

import lombok.Getter;

@Getter
public final class LoginMember {

    private final Long id;
    private final String role;

    public LoginMember(Long id, String role) {
        this.id = id;
        this.role = role;
    }
}
