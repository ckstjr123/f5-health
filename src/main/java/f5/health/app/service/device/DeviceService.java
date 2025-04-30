package f5.health.app.service.device;

import f5.health.app.entity.Device.Device;
import f5.health.app.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    /** 해당 사용자 기기 등록(동일한 식별자를 가진 기존 디바이스 엔티티가 있으면 병합되고 없으면 새로 저장됨) */
    public Device register(Device device) {
        return this.deviceRepository.save(device); // spring data jpa repository save(S entity): persist or merge
    }

}
