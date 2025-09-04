package f5.health.app.meal.controller;

import f5.health.app.common.EnumModel;
import f5.health.app.common.EnumModelMapper;
import f5.health.app.jwt.vo.JwtMember;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.entity.Meal;
import f5.health.app.meal.service.MealService;
import f5.health.app.meal.service.request.MealRequest;
import f5.health.app.meal.vo.MealResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealController implements MealApiDocs {

    private final EnumModelMapper enumMapper;
    private final MealService mealService;

    @GetMapping("/types")
    public List<? extends EnumModel> mealTypes() {
        return enumMapper.get(MealType.class);
    }

    @GetMapping("/{mealId}")
    public MealResponse meal(@PathVariable Long mealId) {
        Meal meal = mealService.findById(mealId);
        return MealResponse.withMealFoods(meal);
    }


    @PostMapping
    public Long save(@AuthenticationPrincipal JwtMember loginMember,
                     @RequestBody @Valid MealRequest mealRequest) {
        return mealService.save(loginMember.getId(), mealRequest);
    }

}
