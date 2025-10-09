package f5.health.app.meal.service;

import f5.health.app.auth.vo.LoginMember;
import f5.health.app.common.exception.*;
import f5.health.app.common.util.Sets;
import f5.health.app.food.entity.Food;
import f5.health.app.food.repository.FoodRepository;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.controller.response.MealDetail;
import f5.health.app.meal.controller.response.MealSummary;
import f5.health.app.meal.controller.response.MealsResponse;
import f5.health.app.meal.domain.Meal;
import f5.health.app.meal.domain.MealFood;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static f5.health.app.common.util.EntityManagerHelper.flushAndClear;
import static f5.health.app.food.FoodErrorCode.NOT_FOUND_FOOD;
import static f5.health.app.meal.domain.Meal.MENU_LIMIT_SIZE_PER_MEAL;
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
        validateMealLimit(memberId, request.eatenAt().toLocalDate(), request.mealType());
        validateRequiredFoods(request.getRequestedFoodIds());

        List<MealFood> mealFoods = createMealFoods(request.mealFoodParams());
        Meal meal = Meal.of(member, request.eatenAt(), request.mealType(), mealFoods);

        mealRepository.save(meal);
        mealFoodRepository.saveAllBatch(mealFoods);
        return meal.getId();
    }

    /** 식사 기록 일괄 수정 */
    public void synchronizeMeal(Long mealId, MealSyncRequest request, Long memberId) {
        Meal meal = findMealById(mealId);
        meal.validateOwnership(memberId);
        validateRequiredFoods(request.getRequestedFoodIds());

        syncMealFoods(request.getNewMealFoodParams(), request.getMealFoodUpdateParams(), request.getToDeleteMealFoodIds(), meal);

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

    private void syncMealFoods(List<MealFoodParam> newParams, List<MealFoodUpdateParam> updateParams, Set<Long> deleteIds, Meal meal) {
        validate(meal.getId(), newParams, updateParams, deleteIds);

        deleteMealFoodByIdIn(deleteIds);
        saveAllNewMealFoods(newParams, meal);
        updateMealFoods(updateParams);

        flushAndClear(em);
    }

    private void validate(Long mealId, List<MealFoodParam> newParams, List<MealFoodUpdateParam> updateParams, Set<Long> deleteIds) {
        Set<Long> requestedIds = new HashSet<>(deleteIds);
        for (MealFoodUpdateParam updateParam : updateParams) {
            boolean isAlreadyContained = !requestedIds.add(updateParam.mealFoodId());
            if (isAlreadyContained) {
                throw new BadRequestException();
            }
        }

        List<MealFood> origins = mealFoodRepository.findByMealId(mealId);
        boolean hasOwnership = origins.stream()
                .map(MealFood::getId)
                .collect(Collectors.toSet())
                .containsAll(requestedIds);
        if (!hasOwnership) {
            throw new AccessDeniedException();
        }

        if ((origins.size() + newParams.size()) - deleteIds.size() > MENU_LIMIT_SIZE_PER_MEAL) {
            throw new ConflictException(EXCEEDED_MAX_MENU_LIMIT);
        }
    }

    private void validateMealLimit(Long memberId, LocalDate eatenDate, MealType mealType) {
        long mealCount = mealRepository.countBy(memberId, eatenDate, mealType);

        if (mealCount >= mealType.maxCountPerDay()) {
            throw new ConflictException(mealLimitExceededErrorCodeFor(mealType));
        }
    }

}
