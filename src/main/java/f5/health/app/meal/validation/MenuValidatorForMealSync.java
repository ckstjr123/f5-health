package f5.health.app.meal.validation;

import f5.health.app.common.exception.BadRequestException;
import f5.health.app.meal.service.request.MealFoodSyncParam;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collections;

import static f5.health.app.common.util.ValidationUtils.buildConstraintViolationMessage;
import static f5.health.app.meal.domain.Meal.MENU_LIMIT_SIZE_PER_MEAL;

public class MenuValidatorForMealSync implements ConstraintValidator<Menu, MealFoodSyncParam> {

    @Override
    public boolean isValid(MealFoodSyncParam param, ConstraintValidatorContext context) {
        if (param.newParams().isEmpty() && param.updateParams().isEmpty() && param.deleteIds().isEmpty()) {
            buildConstraintViolationMessage(context, "전달된 메뉴 사항이 없습니다.");
            return false;
        }

        if (param.deleteIds().size() > MENU_LIMIT_SIZE_PER_MEAL) {
            buildConstraintViolationMessage(context, "메뉴 삭제 요청은 " + MENU_LIMIT_SIZE_PER_MEAL + "개를 초과할 수 없습니다.");
            return false;
        }

        if (param.hasDuplicateUpdate()) {
            throw new BadRequestException("중복된 수정 id가 포함되어 있습니다.");
        }

        if (!Collections.disjoint(param.updateIds(), param.deleteIds())) {
            throw new BadRequestException("수정 요청된 메뉴는 삭제할 수 없습니다.");
        }

        final int requestMenuSize = param.newParams().size() + param.updateParams().size();
        return requestMenuSize <= MENU_LIMIT_SIZE_PER_MEAL;
    }
}
