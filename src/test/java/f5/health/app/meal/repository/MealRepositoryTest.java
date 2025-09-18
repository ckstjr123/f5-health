package f5.health.app.meal.repository;

import f5.health.app.food.repository.FoodRepository;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.entity.Meal;
import f5.health.app.meal.fixture.MealFixture;
import f5.health.app.member.entity.Member;
import f5.health.app.member.fixture.MemberFixture;
import f5.health.app.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MealRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private MealFoodRepository mealFoodRepository;

    @Autowired
    private FoodRepository foodRepository;


    @DisplayName("식단 저장")
    @Test
    void save() {
        Member member = saveMember();
        LocalDateTime eatenAt = LocalDateTime.now();
        MealType mealType = MealType.DINNER;

        Meal savedMeal = mealRepository.save(MealFixture.createMealWithMealFoods(member, eatenAt, mealType));

        Meal findMeal = mealRepository.findById(savedMeal.getId()).orElseThrow();
        assertThat(findMeal.getId()).isEqualTo(savedMeal.getId());
    }

    @DisplayName("식단 상세 조회")
    @Test
    void findMealJoinFetch() {
        Member member = saveMember();
        Meal meal = mealRepository.save(MealFixture.createMealWithMealFoods(member, LocalDateTime.now(), MealType.LUNCH));
        saveMealFoodsOf(meal);

        Meal findMeal = mealRepository.findMealJoinFetch(meal.getId()).orElseThrow();

        assertThat(findMeal).isEqualTo(meal);
    }

    @DisplayName("특정 회원이 해당 일자에 등록한 모든 식단 조회")
    @Test
    void findAll() {
        Member member = saveMember();
        LocalDateTime eatenAt = LocalDateTime.now();
        List<Meal> meals = mealRepository.saveAll(MealFixture.createMealsWithMealFoods(member, eatenAt));

        List<Meal> result = mealRepository.findMeals(member.getId(), eatenAt.toLocalDate());

        assertThat(result).containsExactlyInAnyOrderElementsOf(meals);
    }


    private Member saveMember() {
        return memberRepository.save(MemberFixture.createMember());
    }

    private void saveMealFoodsOf(Meal meal) {
        meal.getMealFoods().forEach(mealFood -> {
            foodRepository.save(mealFood.getFood());
            mealFood.setMeal(meal);
        });
        mealFoodRepository.saveAll(meal.getMealFoods());
    }

}
