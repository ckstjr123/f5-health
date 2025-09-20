package f5.health.app.common.util;

import java.util.HashSet;
import java.util.Set;

public class SetUtils {

    /**
     * @param s1
     * @param s2
     * @param <T>
     * @return s1 - s2
     * @throws NullPointerException
     */
    public static <T> Set<T> difference(Set<T> s1, Set<T> s2) {
        Set<T> difference = new HashSet<>(s1);
        difference.removeAll(s2);
        return difference;
    }
}
