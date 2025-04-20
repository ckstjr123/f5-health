package f5.health.app.exception.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MemberAlreadyJoinedException extends IllegalStateException {

    public MemberAlreadyJoinedException(String message) {
        super(message);
    }
}
