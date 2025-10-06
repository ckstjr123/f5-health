package f5.health.app.meal.controller;

import f5.health.app.auth.Login;
import f5.health.app.auth.vo.LoginMember;
import f5.health.app.common.EnumModel;
import f5.health.app.common.EnumModelMapper;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.controller.response.MealDetail;
import f5.health.app.meal.service.MealService;
import f5.health.app.meal.service.request.MealRequest;
import f5.health.app.meal.service.request.MealSyncRequest;
import f5.health.app.meal.controller.response.MealsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/meals")
    public MealsResponse meals(@Login LoginMember loginMember, @RequestParam("date") LocalDate date) {
        return mealService.findMeals(loginMember.getId(), date);
    }

    @GetMapping("/meal/{mealId}")
    public MealDetail meal(@Login LoginMember loginMember, @PathVariable("mealId") Long mealId) {
        return mealService.getMealDetail(mealId, loginMember);
    }


    @PostMapping("/meal")
    public Long save(@Login LoginMember loginMember, @RequestBody @Valid MealRequest mealRequest) {
        return mealService.saveMeal(loginMember.getId(), mealRequest);
    }

    @PostMapping("/meal/edit")
    public void synchronize(@Login LoginMember loginMember,
                            @RequestBody @Valid MealSyncRequest mealSyncRequest) {
        mealService.synchronizeMeal(loginMember.getId(), mealSyncRequest);
    }

    @DeleteMapping("/meal/{mealId}")
    public ResponseEntity<Void> delete(@Login LoginMember loginMember, @PathVariable("mealId") Long mealId) {
        mealService.deleteMeal(mealId, loginMember);
        return ResponseEntity.noContent().build();
    }

}
