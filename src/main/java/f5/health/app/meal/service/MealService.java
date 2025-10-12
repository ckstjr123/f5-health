package f5.health.app.meal.service;

import f5.health.app.auth.vo.LoginMember;
import f5.health.app.common.exception.AccessDeniedException;
import f5.health.app.common.exception.BadRequestException;
import f5.health.app.common.exception.ConflictException;
import f5.health.app.common.exception.NotFoundException;
import f5.health.app.common.util.Sets;
import f5.health.app.food.entity.Food;
import f5.health.app.food.repository.FoodRepository;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.vo.response.MealDetail;
import f5.health.app.meal.vo.response.MealSummary;
import f5.health.app.meal.vo.response.MealsResponse;
import f5.health.app.meal.domain.Meal;
import f5.health.app.meal.domain.MealFood;
import f5.health.app.meal.repository.MealFoodRepository;
import f5.health.app.meal.repository.MealRepository;
import f5.health.app.meal.service.request.*;
import f5.health.app.member.entity.Member;
import f5.health.app.member.service.MemberService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static f5.health.app.common.util.EntityManagerHelper.flushAndClear;
import static f5.health.app.food.FoodErrorCode.NOT_FOUND_FOOD;
import static f5.health.app.meal.domain.Meal.MENU_MAX_SIZE_PER_MEAL;
import static f5.health.app.meal.domain.Meal.MENU_MIN_SIZE_PER_MEAL;
import static f5.health.app.meal.constant.MealErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MealService {

    private final EntityManager em;
    private final MemberService memberService;
    private final MealRepository mealRepository;
    private final MealFoodRepository mealFoodRepository;
    private final FoodRepository foodRepository;


    public MealsResponse findMeals(Long memberId, LocalDate eatenDate) {
        List<Meal> meals = mealRepository.findMeals(memberId, eatenDate);
        return MealsResponse.from(meals.stream()
                .map(MealSummary::from)
                .toList());
    }

    public MealDetail getMealDetail(Long mealId, LoginMember loginMember) {
        Meal meal = mealRepository.findMealJoinFetch(mealId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEAL, mealId.toString()));
        meal.validateOwnership(loginMember.getId());
        return MealDetail.from(meal);
    }


    // -------------------------------------------------------------------------------------------------------------- //


    /** 식사 기록 */
    public Long saveMeal(Long memberId, MealRequest request) {
        Member member = memberService.findById(memberId);
        validateMealLimitExceeded(memberId, request.eatenAt().toLocalDate(), request.mealType());
        validateRequiredFoods(request.getRequestedFoodIds());

        List<MealFood> mealFoods = createMealFoods(request.mealFoodParams());
        Meal meal = Meal.createMeal(member, request.eatenAt(), request.mealType(), mealFoods);

        mealRepository.save(meal);
        mealFoodRepository.saveAllBatch(mealFoods);
        return meal.getId();
    }

    /** 식사 기록 일괄 수정 */
    public void synchronizeMeal(Long mealId, MealSyncRequest request, Long memberId) {
        Meal meal = findMealById(mealId);
        meal.validateOwnership(memberId);
        validateRequiredFoods(request.getRequestedFoodIds());

        syncMealFoods(request.getMealFoodSyncParam(), meal);

        Meal refreshMeal = findMealById(mealId);
        changeMealTime(refreshMeal, memberId, request.getEatenAt(), request.getMealType());
        refreshMeal.calculateNutrients();
    }

    /** 식사 기록 삭제 */
    public void deleteMeal(Long mealId, LoginMember loginMember) {
        Meal meal = findMealById(mealId);
        meal.validateOwnership(loginMember.getId());
        mealRepository.delete(meal);
    }


    private Meal findMealById(Long mealId) {
        return mealRepository.findById(mealId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEAL, mealId.toString()));
    }

    private void validateRequiredFoods(Set<Long> requestedFoodIds) {
        List<Food> foods = foodRepository.findAllById(requestedFoodIds);
        if (foods.size() != requestedFoodIds.size()) {
            Set<Long> invalidFoodIds = Sets.difference(
                    requestedFoodIds,
                    foods.stream()
                            .map(Food::getId)
                            .collect(Collectors.toSet())
            );
            throw new NotFoundException(NOT_FOUND_FOOD, String.join(", ", invalidFoodIds.toString()));
        }
    }

    private List<MealFood> createMealFoods(List<MealFoodParam> mealFoodParams) {
        return mealFoodParams.stream()
                .map(param -> {
                    Food food = foodRepository.findById(param.foodId()).orElseThrow();
                    return MealFood.of(food, param.count());
                })
                .toList();
    }

    private void changeMealTime(Meal meal, Long memberId, LocalDateTime eatenAt, MealType mealType) {
        if (!meal.hasSameEatenDate(eatenAt.toLocalDate()) || meal.isDifferentTypeFrom(mealType)) {
            validateMealLimitExceeded(memberId, eatenAt.toLocalDate(), mealType);
        }

        meal.updateMealTime(eatenAt, mealType);
    }

    private void saveAllNewMealFoods(List<MealFoodParam> newParams, Meal meal) {
        List<MealFood> newMealFoods = createMealFoods(newParams);
        newMealFoods.forEach(mf -> mf.setMeal(meal));
        mealFoodRepository.saveAllBatch(newMealFoods);
    }

    private void updateMealFoods(List<MealFoodUpdateParam> updateParams) {
        for (var updateParam : updateParams) {
            MealFood mealFood = mealFoodRepository.findById(updateParam.mealFoodId()).orElseThrow();
            Food food = foodRepository.findById(updateParam.foodId()).orElseThrow();

            mealFood.update(food, updateParam.count());
        }
    }

    private void deleteMealFoodByIdIn(Set<Long> ids) {
        if (!ids.isEmpty()) {
            mealFoodRepository.deleteAllByIdInBatch(ids);
        }
    }

    private void syncMealFoods(MealFoodSyncParam param, Meal meal) {
        validate(param, meal.getId());

        deleteMealFoodByIdIn(param.deleteIds());
        saveAllNewMealFoods(param.newParams(), meal);
        updateMealFoods(param.updateParams());

        flushAndClear(em); //
    }

    private void validate(MealFoodSyncParam param, Long mealId) {
        if (param.hasDuplicateUpdate()) {
            throw new BadRequestException(REPEATED_MENU_UPDATE);
        }

        if (!Collections.disjoint(param.updateIds(), param.deleteIds())) {
            throw new BadRequestException(CANNOT_DELETE_EDITED_MENU);
        }

        List<MealFood> origins = mealFoodRepository.findByMealId(mealId);
        if (!containsAll(origins, param.requestedMealFoodIds())) {
            throw new AccessDeniedException();
        }

        int expectedMenuCount = (origins.size() + param.newParams().size()) - param.deleteIds().size();
        if (expectedMenuCount < MENU_MIN_SIZE_PER_MEAL || expectedMenuCount > MENU_MAX_SIZE_PER_MEAL) {
            throw new ConflictException(NOT_ALLOWED_MENU_COUNT);
        }
    }

    private boolean containsAll(List<MealFood> origins, Set<Long> ids) {
        return origins.stream()
                .map(MealFood::getId)
                .collect(Collectors.toSet())
                .containsAll(ids);
    }

    private void validateMealLimitExceeded(Long memberId, LocalDate eatenDate, MealType mealType) {
        long mealCount = mealRepository.countBy(memberId, eatenDate, mealType);

        if (mealCount >= mealType.maxCountPerDay()) {
            throw new ConflictException(getLimitExceededErrorCodeFor(mealType));
        }
    }

}
