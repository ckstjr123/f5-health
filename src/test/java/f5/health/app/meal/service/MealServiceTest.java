package f5.health.app.meal.service;

import f5.health.app.common.exception.DuplicateEntityException;
import f5.health.app.common.exception.NotFoundException;
import f5.health.app.food.entity.Food;
import f5.health.app.food.repository.FoodRepository;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.entity.Meal;
import f5.health.app.meal.entity.MealFood;
import f5.health.app.meal.fixture.MealFixture;
import f5.health.app.meal.fixture.MealRequestFixture;
import f5.health.app.meal.repository.MealFoodRepository;
import f5.health.app.meal.repository.MealRepository;
import f5.health.app.meal.service.request.MealRequest;
import f5.health.app.member.entity.Member;
import f5.health.app.member.fixture.MemberFixture;
import f5.health.app.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static f5.health.app.meal.fixture.MealRequestFixture.createMealRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
public class MealServiceTest {

    @Mock
    private MemberService memberService;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private MealFoodRepository mealFoodRepository;

    @Mock
    private FoodRepository foodRepository;

    @InjectMocks
    private MealService mealService;
    
    @Nested
    @DisplayName("식단 등록")
    class Save {

        @DisplayName("이미 등록된 식단이 있으면 예외")
        @Test
        void validateDuplicateMeal() {
            final Long memberId = 1L;
            Member member = MemberFixture.createMember();
            MealType mealType = MealType.LUNCH;
            LocalDateTime eatenAt = LocalDateTime.now();
            Meal meal = MealFixture.createMealWithMealFoods(member, eatenAt, mealType);
            List<Food> foods = meal.getMealFoods().stream()
                    .map(MealFood::getFood)
                    .toList();

            when(mealRepository.findOne(memberId, eatenAt.toLocalDate(), mealType)).thenReturn(Optional.of(meal));

            assertThrows(DuplicateEntityException.class,
                    () -> mealService.save(memberId, createMealRequest(mealType, eatenAt, foods)));
        }


        @DisplayName("음식 코드에 해당하는 음식을 찾지 못하면 예외")
        @Test
        void getFoodOrElseThrow() {
            final Long memberId = 1L;
            Member member = MemberFixture.createMember();
            MealType mealType = MealType.LUNCH;
            LocalDateTime eatenAt = LocalDateTime.now();
            Meal meal = MealFixture.createMealWithMealFoods(member, eatenAt, mealType);
            List<Food> foods = meal.getMealFoods().stream()
                    .map(MealFood::getFood)
                    .toList();

            MealRequest mealRequest = createMealRequest(mealType, eatenAt, foods);
            when(foodRepository.findByFoodCodeIn(mealRequest.getEatenFoodCodeSet())).thenReturn(new ArrayList<>());

            assertThrows(NotFoundException.class,
                    () -> mealService.save(memberId, createMealRequest(mealType, eatenAt, foods)));
        }
    }

}
