package f5.health.app;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class HealthCareApplicationTests {

    @Test
    void contextLoads() {

        Set<Integer> s1 = new HashSet<>();
        Set<Integer> s2 = new HashSet<>();


        s1.add(1);
        s1.add(2);
        s1.add(3);

        s1.retainAll(s2);

    }



}
