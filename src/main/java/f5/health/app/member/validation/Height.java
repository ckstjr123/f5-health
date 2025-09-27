package f5.health.app.member.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static f5.health.app.member.entity.Member.MAX_HEIGHT;
import static f5.health.app.member.entity.Member.MIN_HEIGHT;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HeightValidator.class)
public @interface Height {

    String message() default "키는 " + MIN_HEIGHT + "~" + MAX_HEIGHT + " 사이로 입력해 주세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
