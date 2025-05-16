package f5.health.app.service.food;

import f5.health.app.entity.Food;
import f5.health.app.repository.FoodRepository;
import f5.health.app.entity.meal.EatenFoodMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;


    public EatenFoodMap findFoodsBy(Set<String> eatenFoodCodeSet) {
        List<Food> eatenFoods = foodRepository.findByFoodCodeIn(eatenFoodCodeSet);
        return new EatenFoodMap(eatenFoods);
    }

}
