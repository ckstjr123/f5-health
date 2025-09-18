package f5.health.app.meal.validation;

import f5.health.app.meal.service.request.MealFoodParam;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Objects;

import static f5.health.app.meal.entity.Meal.MENU_LIMIT_SIZE_PER_MEAL;

public class MealMenuSizeValidator implements ConstraintValidator<MenuSize, List<MealFoodParam>> {

    @Override
    public boolean isValid(List<MealFoodParam> menuList, ConstraintValidatorContext context) {
        if (Objects.isNull(menuList)) {
            return false;
        }

        return (!menuList.isEmpty()) && (menuList.size() <= MENU_LIMIT_SIZE_PER_MEAL);
    }
}
