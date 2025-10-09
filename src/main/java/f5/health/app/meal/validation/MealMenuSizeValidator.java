package f5.health.app.meal.validation;

import f5.health.app.meal.service.request.MealFoodParam;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

import static f5.health.app.meal.domain.Meal.MENU_LIMIT_SIZE_PER_MEAL;

public class MealMenuSizeValidator implements ConstraintValidator<MenuSize, List<MealFoodParam>> {

    @Override
    public boolean isValid(List<MealFoodParam> menus, ConstraintValidatorContext context) {
        return (!menus.isEmpty()) && (menus.size() <= MENU_LIMIT_SIZE_PER_MEAL);
    }
}
