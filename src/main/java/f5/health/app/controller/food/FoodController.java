package f5.health.app.controller.food;

import f5.health.app.entity.Food;
import f5.health.app.exception.global.NotFoundException;
import f5.health.app.repository.FoodRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static f5.health.app.exception.food.FoodErrorCode.NOT_FOUND_FOOD;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(value = "/food", produces = APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class FoodController implements FoodApiDocs {

    private final int FOOD_SEARCH_LIMIT = 15;
    private final FoodRepository foodRepository;

    @GetMapping
    public FoodSearchResponse foods(@RequestParam(name = "foodSearchQuery") @NotBlank String foodSearchQuery) {
        List<Food> foods = foodRepository.findByFoodNameLike(foodSearchQuery + "%", PageRequest.of(0, FOOD_SEARCH_LIMIT));
        return new FoodSearchResponse(foods.stream().map(FoodSearchResponse.FoodSearchResult::new).toList());
    }

    @GetMapping("/{foodCode}")
    public FoodResponse food(@PathVariable("foodCode") String foodCode) {
        Food food = this.foodRepository.findById(foodCode).orElseThrow(() -> new NotFoundException(NOT_FOUND_FOOD));
        return FoodResponse.from(food);
    }

}
