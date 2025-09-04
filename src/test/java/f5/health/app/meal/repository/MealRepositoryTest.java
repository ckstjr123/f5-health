package f5.health.app.meal.repository;

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
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(MealFoodRepository.class)
public class MealRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private MealFoodRepository mealFoodRepository;


    @Test
    void save() {
        Member member = memberRepository.save(MemberFixture.createMember());
        LocalDateTime eatenAt = LocalDateTime.now();
        MealType mealType = MealType.DINNER;
        Meal savedMeal = mealRepository.save(MealFixture.createMealWithMealFoods(member, eatenAt, mealType));

        Meal findMeal = mealRepository.findOne(member.getId(), eatenAt.toLocalDate(), mealType).orElseThrow();

        assertThat(findMeal).isEqualTo(savedMeal);
    }

    @DisplayName("특정 회원이 해당 일자에 등록한 모든 식단 조회")
    @Test
    void findAll() {
        Member member = memberRepository.save(MemberFixture.createMember());
        LocalDateTime eatenAt = LocalDateTime.now();
        List<Meal> meals = mealRepository.saveAll(MealFixture.createMealsWithMealFoods(member, eatenAt));

        List<Meal> result = mealRepository.findAll(member.getId(), eatenAt.toLocalDate());

        assertThat(result).containsExactlyInAnyOrderElementsOf(meals);
    }

}
