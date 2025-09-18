package f5.health.app.meal.validation;

import f5.health.app.meal.service.request.MealSyncRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static f5.health.app.meal.entity.Meal.MENU_LIMIT_SIZE_PER_MEAL;
import static f5.health.app.meal.entity.Meal.MENU_MIN_SIZE_PER_MEAL;

public class MealUpdateMenuSizeValidator implements ConstraintValidator<MenuSize, MealSyncRequest> {

    @Override
    public boolean isValid(MealSyncRequest updateRequest, ConstraintValidatorContext context) {
        int menuListSize = updateRequest.getNewMealFoodParams().size() + updateRequest.getMealFoodUpdateParams().size();
        return (MENU_MIN_SIZE_PER_MEAL <= menuListSize) && (menuListSize <= MENU_LIMIT_SIZE_PER_MEAL);
    }
}
