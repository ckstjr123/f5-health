package f5.health.app.common.util;

import jakarta.persistence.EntityManager;

public final class EntityManagerHelper {

    public static void flushAndClear(EntityManager em) {
        em.flush();
        em.clear();
    }
}
