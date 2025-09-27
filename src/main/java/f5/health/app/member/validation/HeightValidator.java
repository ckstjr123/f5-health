package f5.health.app.member.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static f5.health.app.member.entity.Member.MIN_HEIGHT;
import static f5.health.app.member.entity.Member.MAX_HEIGHT;

public class HeightValidator implements ConstraintValidator<Height, Double> {

    @Override
    public boolean isValid(Double height, ConstraintValidatorContext context) {
        return (MIN_HEIGHT <= height) && (height <= MAX_HEIGHT);
    }
}
