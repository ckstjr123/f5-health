package f5.health.app.repository;

import f5.health.app.entity.Device.Device;
import f5.health.app.entity.Device.DeviceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, DeviceId> {
    // CRUD Device
}
