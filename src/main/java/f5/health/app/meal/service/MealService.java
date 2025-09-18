package f5.health.app.meal.service;

import f5.health.app.auth.exception.AccessDeniedException;
import f5.health.app.common.exception.NotFoundException;
import f5.health.app.food.entity.Food;
import f5.health.app.food.repository.FoodRepository;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.entity.Meal;
import f5.health.app.meal.entity.MealFood;
import f5.health.app.meal.exception.MealLimitExceededException;
import f5.health.app.meal.repository.MealFoodRepository;
import f5.health.app.meal.repository.MealRepository;
import f5.health.app.meal.service.request.MealFoodParam;
import f5.health.app.meal.service.request.MealFoodUpdateParam;
import f5.health.app.meal.service.request.MealRequest;
import f5.health.app.meal.service.request.MealSyncRequest;
import f5.health.app.member.entity.Member;
import f5.health.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static f5.health.app.food.FoodErrorCode.NOT_FOUND_FOOD;
import static f5.health.app.meal.exception.MealErrorCode.NOT_FOUND_MEAL;
import static java.util.function.Function.identity;

@Service
@Transactional
@RequiredArgsConstructor
public class MealService {

    private final MemberService memberService;
    private final MealRepository mealRepository;
    private final MealFoodRepository mealFoodRepository;
    private final FoodRepository foodRepository;


    public List<Meal> findMeals(Long memberId, LocalDate eatenDate) {
        return mealRepository.findMeals(memberId, eatenDate);
    }

    public Meal findWithMealFoods(Long memberId, Long mealId) {
        Meal meal = mealRepository.findMealJoinFetch(mealId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEAL, mealId.toString()));
        validateMealOwner(meal, memberId);
        return meal;
    }


    // ------------------------------------------------------------------------------------------------------------ //


    public Long saveMeal(Long memberId, MealRequest request) {
        Member member = memberService.findById(memberId);
        validateMealLimit(memberId, request.getEatenAt().toLocalDate(), request.getMealType());

        Meal meal = createMealWithMealFoods(member, getEatenFoodMapBy(request.getFoodCodes()), request);

        mealRepository.save(meal);
        mealFoodRepository.saveAllBatch(meal.getMealFoods());
        return meal.getId();
    }

    public void synchronizeMeal(Long memberId, MealSyncRequest request) {
        Long mealId = request.getMealId();
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEAL, mealId.toString()));
        validateMealOwner(meal, memberId);

        changeMealTime(meal, memberId, request.getEatenAt(), request.getMealType());
        syncMealFoods(request.getFoodCodes(), request.getNewMealFoodParams(), request.getMealFoodUpdateParams(), meal);

        meal.calculateNutritionFacts();
    }


    private Map<String, Food> getEatenFoodMapBy(Set<String> foodCodes) {
        Map<String, Food> eatenFoodMap = foodRepository.findByFoodCodeIn(foodCodes).stream()
                .collect(Collectors.toUnmodifiableMap(Food::getFoodCode, identity()));

        if (eatenFoodMap.size() != foodCodes.size()) {
            Set<String> invalidFoodCodes = new HashSet<>(foodCodes);
            invalidFoodCodes.removeAll(eatenFoodMap.keySet());
            throw new NotFoundException(NOT_FOUND_FOOD, String.join(", ", invalidFoodCodes));
        }
        return eatenFoodMap;
    }

    private Meal createMealWithMealFoods(Member member, Map<String, Food> eatenFoodMap, MealRequest mealRequest) {
        List<MealFood> mealFoods = createMealFoods(mealRequest.getMealFoodParams(), eatenFoodMap);
        return Meal.newInstance(member, mealRequest.getEatenAt(), mealRequest.getMealType(), mealFoods);
    }

    private List<MealFood> createMealFoods(List<MealFoodParam> mealFoodParams, Map<String, Food> eatenFoodMap) {
        return mealFoodParams.stream()
                .map(mealFoodParam -> {
                    Food food = eatenFoodMap.get(mealFoodParam.getFoodCode());
                    return MealFood.newInstance(food, mealFoodParam.getCount());
                })
                .toList();
    }

    private void changeMealTime(Meal meal, Long memberId, LocalDateTime eatenAt, MealType mealType) {
        if (!meal.hasSameEatenDate(eatenAt.toLocalDate()) || meal.isDifferentTypeFrom(mealType)) {
            validateMealLimit(memberId, eatenAt.toLocalDate(), mealType);
        }

        meal.updateMealTime(eatenAt, mealType);
    }

    private void saveNewMealFoods(List<MealFoodParam> newParams, Map<String, Food> eatenFoodMap, Meal meal) {
        List<MealFood> newMealFoods = createMealFoods(newParams, eatenFoodMap);
        meal.addAllMealFoods(newMealFoods);
        mealFoodRepository.saveAllBatch(newMealFoods);
    }

    private void updateMealFoods(List<MealFoodUpdateParam> updateParams, Map<String, Food> eatenFoodMap, Map<Long, MealFood> mealFoodById) {
        for (MealFoodUpdateParam updateParam : updateParams) {
            MealFood mealFood = mealFoodById.get(updateParam.getMealFoodId());
            if (mealFood == null) {
                throw new IllegalArgumentException();
            }

            Food food = eatenFoodMap.get(updateParam.getFoodCode());
            mealFood.update(food, updateParam.getCount()); //
        }
    }

    private void deleteMissingMealFoods(Set<Long> deleteIds, List<MealFood> mealFoods) {
        if (!deleteIds.isEmpty()) {
            mealFoodRepository.deleteByIdIn(deleteIds);
            mealFoods.removeIf(mealFood -> deleteIds.contains(mealFood.getId()));
        }
    }

    private void syncMealFoods(Set<String> foodCodes, List<MealFoodParam> newParams, List<MealFoodUpdateParam> updateParams, Meal meal) {
        Map<String, Food> eatenFoodMap = getEatenFoodMapBy(foodCodes);

        saveNewMealFoods(newParams, eatenFoodMap, meal);

        Set<Long> updateIds = updateParams.stream()
                .map(MealFoodUpdateParam::getMealFoodId)
                .collect(Collectors.toUnmodifiableSet());

        Map<Long, MealFood> mealFoodById = meal.getMealFoods().stream()
                .collect(Collectors.toMap(MealFood::getId, identity()));

        HashSet<Long> existingIds = new HashSet<>(mealFoodById.keySet());
        if (!existingIds.containsAll(updateIds)) {
            throw new AccessDeniedException();
        }

        existingIds.removeAll(updateIds);
        Set<Long> deleteIds = existingIds;
        deleteIds.forEach(deleteId -> mealFoodById.remove(deleteId));

        updateMealFoods(updateParams, eatenFoodMap, mealFoodById);
        deleteMissingMealFoods(deleteIds, meal.getMealFoods());
    }

    private void validateMealLimit(Long memberId, LocalDate eatenDate, MealType mealType) {
        long mealCount = mealRepository.countBy(memberId, eatenDate, mealType);

        if (mealCount >= mealType.maxCountPerDay()) {
            throw MealLimitExceededException.forMealType(mealType);
        }
    }

    private void validateMealOwner(Meal meal, Long memberId) {
        if (!meal.isOwnedBy(memberId)) {
            throw new AccessDeniedException();
        }
    }

}
