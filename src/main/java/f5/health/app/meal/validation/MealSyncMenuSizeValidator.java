package f5.health.app.meal.validation;

import f5.health.app.common.exception.BadRequestException;
import f5.health.app.meal.service.request.MealFoodSyncParam;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collections;

import static f5.health.app.common.util.ValidationUtils.buildConstraintViolationMessage;
import static f5.health.app.meal.domain.Meal.MENU_LIMIT_SIZE_PER_MEAL;

public class MealSyncMenuSizeValidator implements ConstraintValidator<MenuSize, MealFoodSyncParam> {

    @Override
    public boolean isValid(MealFoodSyncParam param, ConstraintValidatorContext context) {
        if (param.getNewParams().isEmpty() && param.getUpdateParams().isEmpty() && param.getDeleteIds().isEmpty()) {
            buildConstraintViolationMessage(context, "전달된 메뉴 사항이 없습니다.");
            return false;
        }

        if (!Collections.disjoint(param.getDeleteIds(), param.getUpdateIds())) {
            throw new BadRequestException("수정 요청된 메뉴는 삭제할 수 없습니다.");
        }

        if (param.getDeleteIds().size() > MENU_LIMIT_SIZE_PER_MEAL) {
            buildConstraintViolationMessage(context, "메뉴 삭제 요청은 " + MENU_LIMIT_SIZE_PER_MEAL + "개를 초과할 수 없습니다.");
            return false;
        }

        final int requestMenuSize = param.getNewParams().size() + param.getUpdateParams().size();
        return requestMenuSize <= MENU_LIMIT_SIZE_PER_MEAL;
    }
}
