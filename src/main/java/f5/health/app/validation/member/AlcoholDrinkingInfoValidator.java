package f5.health.app.validation.member;

import f5.health.app.exception.global.BadRequestException;
import f5.health.app.service.healthreport.vo.request.healthkit.CustomHealthKit;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static f5.health.app.exception.member.MemberErrorCode.INVALID_ALCOHOL_DRINKING_INFO;

/**
 * 음주량 정보 검증기
 * @see AlcoholDrinkingInfo
 */
public class AlcoholDrinkingInfoValidator implements ConstraintValidator<AlcoholDrinkingInfo, CustomHealthKit> {

    private static final int ZERO = 0;

    @Override
    public boolean isValid(CustomHealthKit customKit, ConstraintValidatorContext constraintValidatorContext) {
        if (customKit.getConsumedAlcoholDrinks() < ZERO || customKit.getAlcoholCost() < ZERO) {
            return false;
        } else if ((customKit.getConsumedAlcoholDrinks() == ZERO) ^ (customKit.getAlcoholCost() == ZERO)) {
            throw new BadRequestException(INVALID_ALCOHOL_DRINKING_INFO);
        }

        return true;
    }
}
