package f5.health.app.controller.meal;

import f5.health.app.constant.EnumModel;
import f5.health.app.constant.EnumModelMapper;
import f5.health.app.constant.meal.MealType;
import f5.health.app.entity.meal.Meal;
import f5.health.app.exception.global.NotFoundException;
import f5.health.app.repository.MealRepository;
import f5.health.app.vo.meal.response.MealResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static f5.health.app.exception.meal.MealErrorCode.NOT_FOUND_MEAL;

@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealController implements MealApiDocs {

    private final EnumModelMapper enumMapper;
    private final MealRepository mealRepository;

    @GetMapping("/types")
    public List<? extends EnumModel> mealTypes() {
        return enumMapper.get(MealType.class);
    }


    @GetMapping("/{mealId}")
    public MealResponse meal(@PathVariable Long mealId) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEAL, mealId.toString()));
        return MealResponse.withMealFoods(meal);
    }
}
