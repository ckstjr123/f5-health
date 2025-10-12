package f5.health.app.meal.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static f5.health.app.meal.domain.Meal.MENU_MAX_SIZE_PER_MEAL;
import static f5.health.app.meal.domain.Meal.MENU_MIN_SIZE_PER_MEAL;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {
        MenuSizeValidatorForMealSave.class,
        MenuSizeValidatorForMealSync.class
})
public @interface MenuSize {

    String message() default "식단 메뉴를 " + MENU_MIN_SIZE_PER_MEAL + "~" + MENU_MAX_SIZE_PER_MEAL + "개 사이로 입력해 주세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
