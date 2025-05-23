package f5.health.app.controller.food;

import f5.health.app.entity.Food;
import f5.health.app.entity.Member;
import f5.health.app.exception.global.NotFoundException;
import f5.health.app.jwt.JwtMember;
import f5.health.app.repository.FoodRepository;
import f5.health.app.repository.MemberRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static f5.health.app.exception.food.FoodErrorCode.NOT_FOUND_FOOD;
import static f5.health.app.exception.member.MemberErrorCode.NOT_FOUND_MEMBER;

@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
public class FoodController implements FoodApiDocs {

    private final int FOOD_SEARCH_LIMIT_SIZE = 20;
    private final FoodRepository foodRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/search")
    public FoodSearchResponse searchFoods(@RequestParam(name = "foodSearchQuery") @NotBlank String foodSearchQuery) {
        List<Food> foods = foodRepository.findByFoodNameLike(foodSearchQuery + "%", PageRequest.of(0, FOOD_SEARCH_LIMIT_SIZE));
        return new FoodSearchResponse(foods.stream().map(FoodSearchResponse.FoodSearchResult::new).toList());
    }

    @GetMapping("/{foodCode}")
    public FoodResponse food(@PathVariable("foodCode") String foodCode) {
        Food food = foodRepository.findById(foodCode).orElseThrow(() -> new NotFoundException(NOT_FOUND_FOOD));
        return FoodResponse.from(food);
    }

    @GetMapping
    public FoodsResponse foods(@AuthenticationPrincipal JwtMember loginMember,
                               @RequestBody @Valid EatenMealFoodsRequest eatenMealFoodsRequest) {
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));
        List<Food> eatenFoods = foodRepository.findByFoodCodeIn(eatenMealFoodsRequest.getFoodCodeSet());
        return FoodsResponse.of(member.getRecommendedCalories(), eatenFoods);
    }

}
