package f5.health.app.member.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static f5.health.app.member.entity.Member.MIN_WEIGHT;
import static f5.health.app.member.entity.Member.MAX_WEIGHT;


public class WeightValidator implements ConstraintValidator<Weight, Double> {

    @Override
    public boolean isValid(Double weight, ConstraintValidatorContext context) {
        return (MIN_WEIGHT <= weight) && (weight <= MAX_WEIGHT);
    }
}
