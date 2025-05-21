package f5.health.app.entity.meal;

import f5.health.app.entity.Food;
import f5.health.app.exception.food.FoodErrorCode;
import f5.health.app.exception.global.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** 각 음식 코드와 음식이 매핑된 맵(각 식사에서 먹은 음식을 꺼낼 때 사용) */
public final class EatenFoodMap {

    private final Map<String, Food> eatenFoodMap;

    public EatenFoodMap(List<Food> eatenFoods) {
        this.eatenFoodMap = eatenFoods.stream()
                .collect(Collectors.toMap(Food::getFoodCode, Function.identity()));
        // Function.identity(): eatenFood -> eatenFood
    }

    public Food get(String foodCode) {
        Food food = this.eatenFoodMap.get(foodCode);
        if (food == null) {
            throw new NotFoundException(FoodErrorCode.NOT_FOUND_FOOD, foodCode);
        }
        return food;
    }

}
