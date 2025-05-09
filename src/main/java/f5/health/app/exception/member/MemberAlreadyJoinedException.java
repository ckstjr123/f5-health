package f5.health.app.exception.member;

import f5.health.app.exception.global.DuplicateEntityException;

public class MemberAlreadyJoinedException extends DuplicateEntityException {

    public MemberAlreadyJoinedException() {
        super(MemberErrorCode.ALREADY_JOINED_MEMBER);
    }
}
