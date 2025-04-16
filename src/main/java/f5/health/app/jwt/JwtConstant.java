package f5.health.app.jwt;

import lombok.Getter;

@Getter
public enum JwtConstant {

    ACCESS_TOKEN("accessToken"), REFRESH_TOKEN("refreshToken");

    private final String tokenName;

    JwtConstant(String tokenName) {
        this.tokenName = tokenName;
    }
}