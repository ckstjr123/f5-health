package f5.health.app.common.util;

import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityManagerHelper {

    public static void flushAndClear(EntityManager em) {
        em.flush();
        em.clear();
    }
}
