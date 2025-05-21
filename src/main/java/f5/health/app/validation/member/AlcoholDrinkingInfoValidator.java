package f5.health.app.validation.member;

import f5.health.app.entity.Member;
import f5.health.app.exception.global.BadRequestException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static f5.health.app.exception.member.MemberErrorCode.INVALID_ALCOHOL_DRINKING_INFO;

/**
 * 회원 음주 관련 정보 검증기
 * @see AlcoholDrinkingInfo
 */
public class AlcoholDrinkingInfoValidator implements ConstraintValidator<AlcoholDrinkingInfo, Member.MemberCheckUp> {

    private static final int ZERO = 0;

    @Override
    public boolean isValid(Member.MemberCheckUp checkUpMember, ConstraintValidatorContext constraintValidatorContext) {
        if (checkUpMember.getWeekAlcoholDrinks() < ZERO || checkUpMember.getWeekAlcoholCost() < ZERO) {
            return false;
        } else if ((checkUpMember.getWeekAlcoholDrinks() == ZERO) ^ (checkUpMember.getWeekAlcoholCost() == ZERO)) {
            throw new BadRequestException(INVALID_ALCOHOL_DRINKING_INFO);
        }

        return true;
    }
}
