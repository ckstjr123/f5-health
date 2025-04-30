package f5.health.app.constant;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum AuthStatus {

    SIGNUP_REQUIRED(HttpStatus.FOUND),
    JOIN(HttpStatus.CREATED),
    SIGNIN(HttpStatus.OK);

    private final HttpStatusCode httpStatus;

    AuthStatus(HttpStatusCode httpStatus) {
        this.httpStatus = httpStatus;
    }


    public HttpStatusCode httpStatus() {
        return this.httpStatus;
    }
}
