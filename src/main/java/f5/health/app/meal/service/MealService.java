package f5.health.app.meal.service;

import f5.health.app.common.exception.DuplicateEntityException;
import f5.health.app.common.exception.NotFoundException;
import f5.health.app.food.entity.Food;
import f5.health.app.food.repository.FoodRepository;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.entity.Meal;
import f5.health.app.meal.entity.MealFood;
import f5.health.app.meal.repository.MealFoodRepository;
import f5.health.app.meal.repository.MealRepository;
import f5.health.app.meal.service.request.MealFoodRequest;
import f5.health.app.meal.service.request.MealRequest;
import f5.health.app.member.entity.Member;
import f5.health.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static f5.health.app.food.FoodErrorCode.NOT_FOUND_FOOD;
import static f5.health.app.meal.MealErrorCode.DUPLICATED_MEAL;
import static f5.health.app.meal.MealErrorCode.NOT_FOUND_MEAL;

@Service
@Transactional
@RequiredArgsConstructor
public class MealService {

    private final MemberService memberService;
    private final MealRepository mealRepository;
    private final MealFoodRepository mealFoodRepository;
    private final FoodRepository foodRepository;


    public Meal findWithMealFoods(Long mealId) {
        return mealRepository.findMealJoinFetch(mealId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEAL, mealId.toString()));
    }

    public List<Meal> findAll(Long memberId, LocalDate eatenDate) {
        return mealRepository.findAll(memberId, eatenDate);
    }


    // --------------------------------------------------------------------------------------------------- //

    public Long save(Long memberId, MealRequest mealRequest) {
        Member member = memberService.findById(memberId);
        this.validateDuplicateMeal(memberId, mealRequest.getEatenAt(), mealRequest.getMealType());

        List<Food> eatenFoods = foodRepository.findByFoodCodeIn(mealRequest.getEatenFoodCodeSet());
        Meal meal = createMealWithMealFoods(member, toEatenFoodMap(eatenFoods), mealRequest);

        mealRepository.save(meal);
        mealFoodRepository.saveAllMealFoods(meal.getMealFoods());
        return meal.getId();
    }

    public void update() {

    }


    private Map<String, Food> toEatenFoodMap(List<Food> eatenFoods) {
        return eatenFoods.stream()
                .collect(Collectors.toMap(Food::getFoodCode, Function.identity()));
    }

    private Meal createMealWithMealFoods(Member member, Map<String, Food> eatenFoodMap, MealRequest mealRequest) {
        List<MealFood> mealFoods = createMealFoods(eatenFoodMap, mealRequest.getMealFoodRequestList());
        return Meal.newInstance(member, mealRequest.getEatenAt(), mealRequest.getMealType(), mealFoods);
    }

    private List<MealFood> createMealFoods(Map<String, Food> eatenFoodMap, List<MealFoodRequest> mealFoodRequestList) {
        return mealFoodRequestList.stream()
                .map(mealFoodRequest -> {
                    Food food = getFoodOrElseThrow(eatenFoodMap, mealFoodRequest.getFoodCode());
                    return MealFood.newInstance(food, mealFoodRequest.getCount());
                })
                .toList();
    }

    private Food getFoodOrElseThrow(Map<String, Food> eatenFoodMap, String foodCode) {
        Food food = eatenFoodMap.get(foodCode);
        if (food == null) {
            throw new NotFoundException(NOT_FOUND_FOOD, foodCode);
        }
        return food;
    }

    private void validateDuplicateMeal(Long memberId, LocalDateTime eatenAt, MealType mealType) {
        Optional<Meal> meal = mealRepository.findOne(memberId, eatenAt.toLocalDate(), mealType);
        if (!meal.isEmpty()) {
            throw new DuplicateEntityException(DUPLICATED_MEAL);
        }
    }

}
