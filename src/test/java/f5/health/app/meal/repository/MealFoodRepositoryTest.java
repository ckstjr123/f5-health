package f5.health.app.meal.repository;

import f5.health.app.food.entity.Food;
import f5.health.app.food.fixture.FoodFixture;
import f5.health.app.food.repository.FoodRepository;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.domain.Meal;
import f5.health.app.meal.domain.MealFood;
import f5.health.app.meal.fixture.MealFixture;
import f5.health.app.member.entity.Member;
import f5.health.app.member.fixture.MemberFixture;
import f5.health.app.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static f5.health.app.meal.fixture.MealFixture.createMealWithMealFoods;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(MealFoodRepositoryCustomImpl.class)
public class MealFoodRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private MealFoodRepository mealFoodRepository;

    @Test
    void saveAllBatch() {
        Member member = saveMember();
        Meal meal = saveMealOnly(member, LocalDateTime.now(), MealType.BREAKFAST);
        List<Food> foods = saveFoods();
        List<MealFood> mealFoods = MealFixture.createMealFoods(foods);
        mealFoods.forEach(mealFood -> mealFood.setMeal(meal));

        int[] rows = mealFoodRepository.saveAllBatch(mealFoods);

        List<MealFood> savedMealFoods = mealFoodRepository.findAll();
        assertThat(rows.length).isEqualTo(savedMealFoods.size());
        assertThat(savedMealFoods)
                .extracting(MealFood::getId)
                .doesNotContainNull();
    }

    @Test
    void findByMealId() {
        Member member = saveMember();
        Meal meal = saveMealOnly(member, LocalDateTime.now(), MealType.DINNER);
        List<Food> foods = saveFoods();
        meal.addAllMealFoods(MealFixture.createMealFoods(foods));
        List<MealFood> mealFoods = mealFoodRepository.saveAll(meal.getMealFoods());

        List<MealFood> findMealFoods = mealFoodRepository.findByMealId(meal.getId());

        assertThat(findMealFoods).containsExactlyElementsOf(mealFoods);
    }


    private Member saveMember() {
        return memberRepository.save(MemberFixture.createMember());
    }

    private List<Food> saveFoods() {
        return foodRepository.saveAll(FoodFixture.createFoods());
    }

    private Meal saveMealOnly(Member member, LocalDateTime eatenAt, MealType mealType) {
        return mealRepository.save(MealFixture.createMealOnly(member, eatenAt, mealType));
    }

}
