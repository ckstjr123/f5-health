package f5.health.app.constant;

import jakarta.servlet.http.HttpServletResponse;

public enum AuthStatus {
    SIGNUP_REQUIRED(HttpServletResponse.SC_FOUND),
    JOIN(HttpServletResponse.SC_CREATED),
    LOGIN_FAIL(HttpServletResponse.SC_BAD_REQUEST),
    SIGNIN(HttpServletResponse.SC_OK);

    private final int httpStatus;

    AuthStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }
}
