package f5.health.app.meal.service;

import f5.health.app.auth.exception.AccessDeniedException;
import f5.health.app.auth.jwt.vo.JwtMember;
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
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static f5.health.app.common.util.EntityManagerHelper.flushAndClear;
import static f5.health.app.common.util.SetUtils.difference;
import static f5.health.app.food.FoodErrorCode.NOT_FOUND_FOOD;
import static f5.health.app.meal.exception.MealErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MealService {

    private final EntityManager em;
    private final MemberService memberService;
    private final MealRepository mealRepository;
    private final MealFoodRepository mealFoodRepository;
    private final FoodRepository foodRepository;


    public List<Meal> findMeals(Long memberId, LocalDate eatenDate) {
        return mealRepository.findMeals(memberId, eatenDate);
    }

    public Meal findWithMealFoods(Long mealId, JwtMember loginMember) {
        Meal meal = mealRepository.findMealJoinFetch(mealId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEAL, mealId.toString()));
        validateMealOwner(meal, loginMember.id());
        return meal;
    }


    // -------------------------------------------------------------------------------------------------------------- //


    /** 식단 등록 */
    public Long saveMeal(Long memberId, MealRequest request) {
        Member member = memberService.findById(memberId);
        validateMealLimit(memberId, request.eatenAt().toLocalDate(), request.mealType());
        validateRequiredFoods(request.getFoodCodes());

        Meal meal = createMealWithMealFoods(request, member);

        mealRepository.save(meal);
        mealFoodRepository.saveAllBatch(meal.getMealFoods());
        return meal.getId();
    }

    /** 식단 일괄 수정 */
    public void synchronizeMeal(Long memberId, MealSyncRequest request) {
        Long mealId = request.mealId();
        Meal meal = findMealById(mealId);
        validateMealOwner(meal, memberId);
        validateRequiredFoods(request.getFoodCodes());

        syncMealFoods(request.newMealFoodParams(), request.mealFoodUpdateParams(), meal);

        Meal refreshMeal = findMealById(mealId);
        changeMealTime(refreshMeal, memberId, request.eatenAt(), request.mealType());
        refreshMeal.calculateNutritionFacts();
    }

    /** 식단 삭제 */
    public void deleteMeal(Long mealId, JwtMember loginMember) {
        Meal meal = findMealById(mealId);
        validateMealOwner(meal, loginMember.id());
        mealRepository.delete(meal);
    }


    private Meal findMealById(Long mealId) {
        return mealRepository.findById(mealId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEAL, mealId.toString()));
    }

    private void validateRequiredFoods(Set<String> foodCodes) {
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
        List<MealFood> mealFoods = createMealFoods(mealRequest.mealFoodParams());
        return Meal.newInstance(member, mealRequest.eatenAt(), mealRequest.mealType(), mealFoods);
    }

    private List<MealFood> createMealFoods(List<MealFoodParam> mealFoodParams) {
        return mealFoodParams.stream()
                .map(param -> {
                    Food food = foodRepository.findById(param.foodCode()).orElseThrow();
                    return MealFood.newInstance(food, param.count());
                })
                .toList();
    }

    private void changeMealTime(Meal meal, Long memberId, LocalDateTime eatenAt, MealType mealType) {
        if (!meal.hasSameEatenDate(eatenAt.toLocalDate()) || meal.isDifferentTypeFrom(mealType)) {
            validateMealLimit(memberId, eatenAt.toLocalDate(), mealType);
        }

        meal.updateMealTime(eatenAt, mealType);
    }

    private void saveAllNewMealFoods(List<MealFoodParam> newParams, Meal meal) {
        List<MealFood> newMealFoods = createMealFoods(newParams);
        newMealFoods.forEach(mf -> mf.setMeal(meal));
        mealFoodRepository.saveAllBatch(newMealFoods);
    }

    private void updateMealFoods(List<MealFoodUpdateParam> updateParams) {
        for (MealFoodUpdateParam updateParam : updateParams) {
            MealFood mealFood = mealFoodRepository.findById(updateParam.mealFoodId())
                    .orElseThrow(IllegalArgumentException::new);
            Food food = foodRepository.findById(updateParam.foodCode()).orElseThrow();

            mealFood.update(food, updateParam.count());
        }
    }

    private void deleteMealFoodByIdIn(Set<Long> ids) {
        if (!ids.isEmpty()) {
            mealFoodRepository.deleteAllByIdInBatch(ids);
        }
    }

    private void syncMealFoods(List<MealFoodParam> newParams, List<MealFoodUpdateParam> updateParams, Meal meal) {
        List<MealFood> mealFoods = mealFoodRepository.findByMealId(meal.getId());

        Set<Long> originalIds = mealFoods.stream()
                .map(MealFood::getId)
                .collect(Collectors.toUnmodifiableSet());

        Set<Long> updateIds = updateParams.stream()
                .map(MealFoodUpdateParam::mealFoodId)
                .collect(Collectors.toUnmodifiableSet());

        if (!originalIds.containsAll(updateIds)) {
            throw new AccessDeniedException(NOT_FOUND_MEAL_FOOD_OWNERSHIP);
        }

        deleteMealFoodByIdIn(difference(originalIds, updateIds));
        saveAllNewMealFoods(newParams, meal);
        updateMealFoods(updateParams);

        flushAndClear(em);
    }

    private void validateMealLimit(Long memberId, LocalDate eatenDate, MealType mealType) {
        long mealCount = mealRepository.countBy(memberId, eatenDate, mealType);

        if (mealCount >= mealType.maxCountPerDay()) {
            throw MealLimitExceededException.forMealType(mealType);
        }
    }

    private void validateMealOwner(Meal meal, Long memberId) {
        if (!meal.isOwnedBy(memberId)) {
            throw new AccessDeniedException(NOT_FOUND_MEAL_OWNERSHIP);
        }
    }

}
