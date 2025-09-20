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
import java.util.*;
import java.util.stream.Collectors;

import static f5.health.app.food.FoodErrorCode.NOT_FOUND_FOOD;
import static f5.health.app.meal.exception.MealErrorCode.NOT_FOUND_MEAL;

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


    // ----------------------------------------------------------------------------------------------------------------- //


    public Long saveMeal(Long memberId, MealRequest request) {
        Member member = memberService.findById(memberId);
        validateMealLimit(memberId, request.getEatenAt().toLocalDate(), request.getMealType());
        validateAllFoodBy(request.getFoodCodes());

        Meal meal = createMealWithMealFoods(request, member);

        mealRepository.save(meal);
        mealFoodRepository.saveAllBatch(meal.getMealFoods());
        return meal.getId();
    }

    public void synchronizeMeal(Long memberId, MealSyncRequest request) {
        Long mealId = request.getMealId();
        Meal meal = findMealById(request.getMealId());
        validateMealOwner(meal, memberId);
        validateAllFoodBy(request.getFoodCodes());

        syncMealFoods(request.getNewMealFoodParams(), request.getMealFoodUpdateParams(), meal);

        Meal refreshMeal = findMealById(mealId);
        changeMealTime(refreshMeal, memberId, request.getEatenAt(), request.getMealType());
        refreshMeal.calculateNutritionFacts();
    }


    private Meal findMealById(Long mealId) {
        return mealRepository.findById(mealId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEAL, mealId.toString()));
    }

    private void validateAllFoodBy(Set<String> foodCodes) {
        List<Food> foods = foodRepository.findAllById(foodCodes);
        if (foods.size() != foodCodes.size()) {
            Set<String> invalidFoodCodes = difference(
                    foodCodes,
                    foods.stream()
                         .map(Food::getFoodCode)
                         .collect(Collectors.toSet())
            );
            throw new NotFoundException(NOT_FOUND_FOOD, String.join(", ", invalidFoodCodes));
        }
    }

    private Meal createMealWithMealFoods(MealRequest mealRequest, Member member) {
        List<MealFood> mealFoods = createMealFoods(mealRequest.getMealFoodParams());
        return Meal.newInstance(member, mealRequest.getEatenAt(), mealRequest.getMealType(), mealFoods);
    }

    private List<MealFood> createMealFoods(List<MealFoodParam> mealFoodParams) {
        return mealFoodParams.stream()
                .map(param -> {
                    Food food = foodRepository.findById(param.getFoodCode()).orElseThrow();
                    return MealFood.newInstance(food, param.getCount());
                })
                .toList();
    }

    private void changeMealTime(Meal meal, Long memberId, LocalDateTime eatenAt, MealType mealType) {
        if (!meal.hasSameEatenDate(eatenAt.toLocalDate()) || meal.isDifferentTypeFrom(mealType)) {
            validateMealLimit(memberId, eatenAt.toLocalDate(), mealType);
        }

        meal.updateMealTime(eatenAt, mealType);
    }

    // TODO: 삽입
    private void saveNewMealFoods(List<MealFoodParam> newParams, Meal meal) {
        List<MealFood> newMealFoods = createMealFoods(newParams);
        newMealFoods.forEach(mf -> mf.setMeal(meal));
        mealFoodRepository.saveAllBatch(newMealFoods);
    }

    private void updateMealFoods(List<MealFoodUpdateParam> updateParams) {
        for (MealFoodUpdateParam updateParam : updateParams) {
            MealFood mealFood = mealFoodRepository.findById(updateParam.getMealFoodId())
                    .orElseThrow(IllegalArgumentException::new);
            Food food = foodRepository.findById(updateParam.getFoodCode()).orElseThrow();
            mealFood.update(food, updateParam.getCount());
        }
    }

    private void deleteMissingMealFoods(Set<Long> existingIds, Set<Long> updateIds) {
        Set<Long> idsToDelete = difference(existingIds, updateIds);
        if (!idsToDelete.isEmpty()) {
            mealFoodRepository.deleteByIdIn(idsToDelete);
        }
    }

    // TODO: update, delete, insert
    private void syncMealFoods(List<MealFoodParam> newParams, List<MealFoodUpdateParam> updateParams, Meal meal) {
        List<MealFood> mealFoods = mealFoodRepository.findByMealId(meal.getId());

        Set<Long> existingIds = mealFoods.stream()
                .map(MealFood::getId)
                .collect(Collectors.toUnmodifiableSet());

        Set<Long> updateIds = updateParams.stream()
                .map(MealFoodUpdateParam::getMealFoodId)
                .collect(Collectors.toUnmodifiableSet());

        if (!existingIds.containsAll(updateIds)) {
            throw new AccessDeniedException();
        }

        updateMealFoods(updateParams);

        // flush update/delete and clear
        deleteMissingMealFoods(existingIds, updateIds);
        saveNewMealFoods(newParams, meal);
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

    /**
     * @param s1
     * @param s2
     * @return s1 - s2
     * @param <T>
     */
    public <T> Set<T> difference(Set<T> s1, Set<T> s2) {
        Set<T> difference = new HashSet<>(s1);
        difference.removeAll(s2);
        return difference;
    }

}
