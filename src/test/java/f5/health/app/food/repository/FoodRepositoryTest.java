package f5.health.app.food.repository;

import f5.health.app.food.fixture.FoodFixture;
import f5.health.app.food.entity.Food;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FoodRepositoryTest {

    @Autowired
    FoodRepository foodRepository;

    @Test
    void findByFoodCodeIn() {
        List<Food> foods = foodRepository.saveAll(Set.of(
                FoodFixture.createBasicFood("R211-927054101-1201", "음식1"),
                FoodFixture.createHighProteinFood("R211-927054101-1202", "음식2"))
        );

        List<Food> findFoods = foodRepository.findByFoodCodeIn(foods.stream().map(Food::getFoodCode).collect(Collectors.toSet()));

        for (Food findFood : findFoods) {
            System.out.println(findFood.getFoodCode());
        }

        assertThat(findFoods).containsExactlyInAnyOrderElementsOf(foods);
    }

    @Test
    void findByFoodNameLike() {
        Food food = foodRepository.save(FoodFixture.createBasicFood("R211-927054101-1205", "음식"));

        List<Food> foods = foodRepository.findByFoodNameLike(food.getFoodName() + "%", PageRequest.of(0, 1));

        assertThat(foods).containsExactly(food);
    }

}
