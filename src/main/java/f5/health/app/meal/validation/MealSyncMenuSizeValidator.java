package f5.health.app.meal.validation;

import f5.health.app.meal.service.request.MealSyncRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static f5.health.app.meal.entity.Meal.MENU_LIMIT_SIZE_PER_MEAL;
import static f5.health.app.meal.entity.Meal.MENU_MIN_SIZE_PER_MEAL;

public class MealSyncMenuSizeValidator implements ConstraintValidator<MenuSize, MealSyncRequest> {

    @Override
    public boolean isValid(MealSyncRequest syncRequest, ConstraintValidatorContext context) {
        int menuSize = syncRequest.getNewMealFoodParams().size() + syncRequest.getMealFoodUpdateParams().size();
        return (MENU_MIN_SIZE_PER_MEAL <= menuSize) && (menuSize <= MENU_LIMIT_SIZE_PER_MEAL);
    }
}
