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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

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
    void saveAllMealFoods() {
        Member member = createMember();
        Meal meal = createMealWithMealFoods(member);
        List<MealFood> mealFoods = meal.getMealFoods();
        mealFoods.forEach(mealFood -> {
            foodRepository.save(mealFood.getFood());
            mealFood.setMeal(meal);
        });
        mealRepository.save(meal);

        int[] rows = mealFoodRepository.saveAllMealFoods(mealFoods);

        assertThat(rows.length).isEqualTo(mealFoods.size());
    }


    @Test
    void deleteByMealId() {
        Member member = createMember();
        Meal meal = createMealWithMealFoods(member);
        List<MealFood> mealFoods = meal.getMealFoods();
        mealFoods.forEach(mealFood -> {
            foodRepository.save(mealFood.getFood());
            mealFood.setMeal(meal);
        });
        mealRepository.save(meal);
        mealFoodRepository.saveAllMealFoods(mealFoods);

        mealFoodRepository.deleteByMealId(meal.getId());

        em.refresh(meal);
        assertThat(meal.getMealFoods()).isEmpty();
    }


    private Member createMember() {
        return memberRepository.save(MemberFixture.createMember());
    }

    private Meal createMealWithMealFoods(Member member) {
        return MealFixture.createMealWithMealFoods(member, LocalDateTime.now(), MealType.DESSERT);
    }

}
