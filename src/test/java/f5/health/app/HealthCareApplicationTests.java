package f5.health.app;

import f5.health.app.common.util.SetUtils;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class HealthCareApplicationTests {

	@Test
	void contextLoads() {
        HashSet<Integer> objects = new HashSet<>(Set.of(1,null));
        SetUtils.difference(objects, Set.of(1));
    }

}
