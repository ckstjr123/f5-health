package f5.health.app.food.repository;

import f5.health.app.food.entity.Food;
import f5.health.app.food.fixture.FoodFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FoodRepositoryTest {

    @Autowired
    private FoodRepository foodRepository;

    @Test
    void findByFoodNameLike() {
        Food food = foodRepository.save(FoodFixture.createBasicFood("식품"));

        List<Food> foods = foodRepository.findByFoodNameLike(food.getFoodName() + "%", PageRequest.of(0, 1));

        assertThat(foods).containsExactly(food);
    }

}
