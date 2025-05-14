package f5.health.app;

import f5.health.app.repository.MealRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@SpringBootTest
@DataJpaTest
class HealthApplicationTests {

	@Autowired
	private MealRepository repository;

	@Test
	void contextLoads() {
	}
}

