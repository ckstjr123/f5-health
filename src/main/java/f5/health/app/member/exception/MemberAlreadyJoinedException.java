package f5.health.app.member.exception;

import f5.health.app.common.exception.DuplicateEntityException;

public class MemberAlreadyJoinedException extends DuplicateEntityException {

    public MemberAlreadyJoinedException() {
        super(MemberErrorCode.ALREADY_JOINED_MEMBER);
    }
}
