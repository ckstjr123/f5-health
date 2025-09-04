package f5.health.app.auth.constant;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum OAuth2LoginStatus {

    CHECKUP_REQUIRED(HttpStatus.FOUND),
    OAUTH2_LOGIN_SUCCESS(HttpStatus.OK);

    private final HttpStatusCode httpStatus;

    OAuth2LoginStatus(HttpStatusCode httpStatus) {
        this.httpStatus = httpStatus;
    }


    public HttpStatusCode httpStatus() {
        return this.httpStatus;
    }
}
