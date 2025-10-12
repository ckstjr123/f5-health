package f5.health.app.meal.validation;

import f5.health.app.meal.service.request.MealFoodSyncParam;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static f5.health.app.common.util.ValidationUtils.buildConstraintViolationMessage;
import static f5.health.app.meal.domain.Meal.MENU_MAX_SIZE_PER_MEAL;

public class MenuSizeValidatorForMealSync implements ConstraintValidator<MenuSize, MealFoodSyncParam> {

    @Override
    public boolean isValid(MealFoodSyncParam param, ConstraintValidatorContext context) {
        if (param.newParams().isEmpty() && param.updateParams().isEmpty() && param.deleteIds().isEmpty()) {
            buildConstraintViolationMessage(context, "전달된 메뉴 사항이 없습니다.");
            return false;
        }

        if (param.deleteIds().size() > MENU_MAX_SIZE_PER_MEAL) {
            buildConstraintViolationMessage(context, "메뉴는 한번에 " + MENU_MAX_SIZE_PER_MEAL + "개까지 삭제 가능합니다.");
            return false;
        }

        final int requestMenuSize = param.newParams().size() + param.updateParams().size();
        return requestMenuSize <= MENU_MAX_SIZE_PER_MEAL;
    }
}
