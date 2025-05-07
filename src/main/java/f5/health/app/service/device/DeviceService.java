package f5.health.app.service.device;

import f5.health.app.vo.device.DeviceAndMemberRole;
import f5.health.app.entity.Device.Device;
import f5.health.app.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    /** 해당 사용자 기기 등록(동일한 식별자를 가진 기존 디바이스 엔티티가 있으면 병합되고 없으면 새로 저장됨) */
    public Device register(Device device) {
        return this.deviceRepository.save(device); // spring data jpa repository save(S entity): persist or merge
    }

    /** 토큰 재발급 시 사용 */
    public Optional<DeviceAndMemberRole> findDeviceAndMemberRoleBy(String refreshToken) {
        return this.deviceRepository.findDeviceAndMemberRoleByRefreshToken(refreshToken);
    }

    /** 로그아웃 시 사용 */
    public void deleteByMemberIdAndRefreshToken(Long memberId, String refreshToken) {
        this.deviceRepository.deleteByDeviceIdMemberIdAndRefreshToken(memberId, refreshToken);
    }

}
