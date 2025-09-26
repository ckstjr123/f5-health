package f5.health.app.meal.repository;

import f5.health.app.food.repository.FoodRepository;
import f5.health.app.meal.constant.MealType;
import f5.health.app.meal.entity.Meal;
import f5.health.app.meal.entity.MealFood;
import f5.health.app.meal.fixture.MealFixture;
import f5.health.app.member.entity.Member;
import f5.health.app.member.fixture.MemberFixture;
import f5.health.app.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
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

    @Autowired
    private EntityManager em;


    @Test
    void saveAllBatch() {
        Member member = memberRepository.save(MemberFixture.createMember());
        Meal meal = createMealWithMealFoods(member, LocalDateTime.now(), MealType.BREAKFAST);
        meal.getMealFoods().forEach(mealFood -> {
            foodRepository.save(mealFood.getFood());
            mealFood.setMeal(meal);
        });
        mealRepository.save(meal);

        int[] rows = mealFoodRepository.saveAllBatch(meal.getMealFoods());

        List<MealFood> savedMealFoods = mealFoodRepository.findAll();
        assertThat(rows.length).isEqualTo(savedMealFoods.size());
        assertThat(savedMealFoods)
                .extracting(MealFood::getId)
                .doesNotContainNull();
    }

    @Test
    void findByMealId() {
        Meal meal = saveMealWithMealFoods();

        List<MealFood> mealFoods = mealFoodRepository.findByMealId(meal.getId());

        assertThat(mealFoods).containsExactlyElementsOf(meal.getMealFoods());
    }


    private Meal saveMealWithMealFoods() {
        Member member = memberRepository.save(MemberFixture.createMember());
        Meal meal = MealFixture.createMealWithMealFoods(member, LocalDateTime.now(), MealType.LUNCH);
        mealRepository.save(meal);

        meal.getMealFoods().forEach(mealFood -> {
            foodRepository.save(mealFood.getFood());
            mealFood.setMeal(meal);
        });
        mealFoodRepository.saveAll(meal.getMealFoods());
        return meal;
    }

    private Set<Long> collectIds(List<MealFood> mealFoods) {
        return mealFoods.stream()
                .map(MealFood::getId)
                .collect(Collectors.toUnmodifiableSet());
    }

}
