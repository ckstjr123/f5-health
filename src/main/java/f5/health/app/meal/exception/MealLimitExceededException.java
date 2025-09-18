package f5.health.app.meal.exception;

import f5.health.app.common.exception.ConflictException;
import f5.health.app.meal.constant.MealType;

import static f5.health.app.meal.exception.MealErrorCode.mealLimitExceededErrorCodeFor;

public class MealLimitExceededException extends ConflictException {

    private MealLimitExceededException(MealErrorCode errorCode) {
        super(errorCode);
    }

    public static MealLimitExceededException forMealType(MealType mealType) {
        return new MealLimitExceededException(mealLimitExceededErrorCodeFor(mealType));
    }
}
