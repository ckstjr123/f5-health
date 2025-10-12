package f5.health.app.activity.repository;

import f5.health.app.activity.domain.alcoholconsumption.AlcoholConsumption;
import f5.health.app.activity.domain.alcoholconsumption.AlcoholConsumptionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlcoholConsumptionRepository extends JpaRepository<AlcoholConsumption, AlcoholConsumptionId> {
}
