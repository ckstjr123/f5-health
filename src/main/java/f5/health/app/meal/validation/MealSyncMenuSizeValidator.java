package f5.health.app.meal.validation;

import f5.health.app.meal.service.request.MealSyncRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static f5.health.app.common.util.ValidationUtils.buildConstraintViolationMessage;
import static f5.health.app.meal.domain.Meal.MENU_LIMIT_SIZE_PER_MEAL;
import static f5.health.app.meal.domain.Meal.MENU_MIN_SIZE_PER_MEAL;

public class MealSyncMenuSizeValidator implements ConstraintValidator<MenuSize, MealSyncRequest> {

    @Override
    public boolean isValid(MealSyncRequest request, ConstraintValidatorContext context) {
        if (request.getToDeleteMealFoodIds().size() > MENU_LIMIT_SIZE_PER_MEAL) {
            buildConstraintViolationMessage(context, "삭제할 메뉴는 " + MENU_LIMIT_SIZE_PER_MEAL + "개를 초과할 수 없습니다.");
            return false;
        }

        final int menuSize = request.getNewMealFoodParams().size() + request.getMealFoodUpdateParams().size();
        return (MENU_MIN_SIZE_PER_MEAL <= menuSize) && (menuSize <= MENU_LIMIT_SIZE_PER_MEAL);
    }
}
