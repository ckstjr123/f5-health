package f5.health.app.meal.controller;

import f5.health.app.common.EnumModel;
import f5.health.app.common.EnumModelMapper;
import f5.health.app.auth.jwt.vo.JwtMember;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.entity.Meal;
import f5.health.app.meal.service.MealService;
import f5.health.app.meal.service.request.MealRequest;
import f5.health.app.meal.vo.MealResponse;
import f5.health.app.meal.vo.MealsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class MealController implements MealApiDocs {

    private final EnumModelMapper enumMapper;
    private final MealService mealService;

    @GetMapping("/meal/types")
    public List<? extends EnumModel> mealTypes() {
        return enumMapper.get(MealType.class);
    }

    @GetMapping("/meal/{mealId}")
    public MealResponse meal(@PathVariable("mealId") Long mealId) {
        Meal meal = mealService.findWithMealFoods(mealId);
        return MealResponse.withMealFoods(meal);
    }

    @GetMapping("/meals")
    public MealsResponse meals(@AuthenticationPrincipal JwtMember loginMember, @RequestParam("date") LocalDate date) {
        List<Meal> meals = mealService.findAll(loginMember.getId(), date);
        return MealsResponse.from(meals.stream()
                .map(MealResponse::withoutMealFoods)
                .toList());
    }


    // ------------------------------------------------------------------------------------------------------------------------ //

    @PostMapping("/meal")
    public Long save(@AuthenticationPrincipal JwtMember loginMember,
                     @RequestBody @Valid MealRequest mealRequest) {
        return mealService.save(loginMember.getId(), mealRequest);
    }

}
