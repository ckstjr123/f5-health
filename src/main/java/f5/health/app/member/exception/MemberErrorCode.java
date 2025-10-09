package f5.health.app.member.exception;

import f5.health.app.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum MemberErrorCode implements ErrorCode {

    NOT_FOUND_MEMBER("존재하지 않는 회원입니다."),
    ALREADY_REGISTERED_MEMBER("이미 가입된 회원입니다.");

    private final String code;
    private final String message;

    MemberErrorCode(String message) {
        this.code = this.name();
        this.message = message;
    }
}
