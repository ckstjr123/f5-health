package f5.health.app.meal.repository;

import f5.health.app.food.repository.FoodRepository;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.domain.Meal;
import f5.health.app.meal.domain.MealFood;
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

import static f5.health.app.meal.fixture.MealFixture.createMealWithMealFoods;
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


    @Test
    void countBy() {
        Member member = saveMember();
        Meal meal = saveMealOnly(member, LocalDateTime.now(), MealType.SNACK);

        long count = mealRepository.countBy(member.getId(), meal.getEatenDate(), meal.getMealType());

        assertThat(count).isOne();
    }

    @DisplayName("식단 저장")
    @Test
    void save() {
        Meal savedMeal = saveMealOnly(saveMember(), LocalDateTime.now(), MealType.DINNER);

        Meal findMeal = mealRepository.findById(savedMeal.getId()).orElseThrow();
        assertThat(findMeal.getId()).isEqualTo(savedMeal.getId());
    }

    @DisplayName("식단 상세 조회")
    @Test
    void findMealJoinFetch() {
        Meal meal = mealRepository.save(MealFixture.createMealWithMealFoods(saveMember(), LocalDateTime.now(), MealType.LUNCH));
        foodRepository.saveAll(meal.getMealFoods().stream()
                .map(MealFood::getFood)
                .toList());
        mealFoodRepository.saveAll(meal.getMealFoods());

        Meal findMeal = mealRepository.findMealJoinFetch(meal.getId()).orElseThrow();

        assertThat(findMeal).isEqualTo(meal);
    }

    @DisplayName("특정 회원이 해당 일자에 등록한 모든 식단 조회")
    @Test
    void findAll() {
        Member member = saveMember();
        LocalDateTime eatenAt = LocalDateTime.now();
        List<Meal> meals = mealRepository.saveAll(List.of(
                MealFixture.createMealOnly(member, eatenAt, MealType.LUNCH),
                MealFixture.createMealOnly(member, eatenAt, MealType.SNACK))
        );

        List<Meal> result = mealRepository.findMeals(member.getId(), eatenAt.toLocalDate());

        assertThat(result).containsExactlyInAnyOrderElementsOf(meals);
    }


    private Member saveMember() {
        return memberRepository.save(MemberFixture.createMember());
    }

    private Meal saveMealOnly(Member member, LocalDateTime eatenAt, MealType mealType) {
        return mealRepository.save(MealFixture.createMealOnly(member, eatenAt, mealType));
    }

}
