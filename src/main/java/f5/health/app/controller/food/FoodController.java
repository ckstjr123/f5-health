package f5.health.app.controller.food;

import f5.health.app.entity.Food;
import f5.health.app.exception.global.NotFoundException;
import f5.health.app.repository.FoodRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static f5.health.app.exception.food.FoodErrorCode.NOT_FOUND_FOOD;

@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
public class FoodController implements FoodApiDocs {

    private final int FOOD_SEARCH_LIMIT_SIZE = 15;
    private final FoodRepository foodRepository;

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
    public FoodsResponse foods(@RequestBody @Valid EatenMealFoodsRequest eatenMealFoodsRequest) {
        List<Food> eatenFoods = foodRepository.findByFoodCodeIn(eatenMealFoodsRequest.getFoodCodeSet());
        return FoodsResponse.from(eatenFoods);
    }

}
