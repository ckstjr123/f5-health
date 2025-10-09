package f5.health.app.member.exception;

import f5.health.app.common.exception.ConflictException;

public class MemberAlreadyRegisteredException extends ConflictException {

    public MemberAlreadyRegisteredException() {
        super(MemberErrorCode.ALREADY_REGISTERED_MEMBER);
    }
}
