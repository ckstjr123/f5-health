package f5.health.app.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@NotNull
@Positive
@Constraint(validatedBy = {})
public @interface PrimaryKey {

    String message() default "식별자 값이 유효하지 않음";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
