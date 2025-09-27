package f5.health.app.member.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static f5.health.app.member.entity.Member.MIN_WEIGHT;
import static f5.health.app.member.entity.Member.MAX_WEIGHT;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WeightValidator.class)
public @interface Weight {

    String message() default "체중은 " + MIN_WEIGHT + "~" + MAX_WEIGHT + " 사이로 입력해 주세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
