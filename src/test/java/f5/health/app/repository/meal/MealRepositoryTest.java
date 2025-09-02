package f5.health.app.repository.meal;

import f5.health.app.meal.constant.MealType;
import f5.health.app.member.constant.Role;
import f5.health.app.food.entity.Food;
import f5.health.app.meal.entity.Meal;
import f5.health.app.meal.entity.MealFood;
import f5.health.app.meal.repository.MealRepository;
import f5.health.app.member.entity.Member;
import f5.health.app.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class MealRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MealRepository mealRepository;


    @DisplayName("식단 등록")
    @Test
    void save() {
        Member member = createMember();
        LocalDateTime eatenAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
        MealType mealType = MealType.DINNER;

        mealRepository.save(Meal.newInstance(member, eatenAt, mealType, List.of(MealFood.newInstance(createFood(), 1.0))));

        LocalDate eatenDate = eatenAt.toLocalDate();
        Meal meal = mealRepository.findOne(member.getId(), eatenDate, mealType).orElseThrow();
        assertEquals(member.getId(), meal.getMember().getId());
        assertEquals(eatenAt, meal.getEatenAt());
        assertEquals(eatenDate, meal.getEatenDate());
        assertEquals(mealType, meal.getMealType());
    }


    private Member createMember() {
        Member.CheckUp memberCheckUp = Member.CheckUp.builder().build();
        Member member = Member.createMember("OAuthId", "email", "nickname", Role.USER, memberCheckUp);
        memberRepository.save(member);
        return member;
    }

    private Food createFood() {
        return Food.createFood()
                .foodCode("D303-148170000-0001")
                .foodName("냉면")
                .kcal(100)
                .carbohydrate(13.15)
                .protein(2.5)
                .fat(3.2)
                .sugar(0.82)
                .natrium(415)
                .nutritionContentStdQuantity("100")
                .foodWeight("550g")
                .build();
    }

}
