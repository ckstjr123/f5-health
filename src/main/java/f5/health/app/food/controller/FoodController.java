package f5.health.app.food.controller;

import f5.health.app.food.vo.FoodResponse;
import f5.health.app.food.vo.FoodSearchResponse;
import f5.health.app.food.entity.Food;
import f5.health.app.common.exception.NotFoundException;
import f5.health.app.food.repository.FoodRepository;
import f5.health.app.member.repository.MemberRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static f5.health.app.food.FoodErrorCode.NOT_FOUND_FOOD;

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

    @GetMapping("/{foodId}")
    public FoodResponse food(@PathVariable("foodId") Long foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new NotFoundException(NOT_FOUND_FOOD));
        return FoodResponse.from(food);
    }

}
