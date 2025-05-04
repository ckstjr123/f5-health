package f5.health.app.repository;

import f5.health.app.dto.device.DeviceAndMemberRole;
import f5.health.app.entity.Device.Device;
import f5.health.app.entity.Device.DeviceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, DeviceId> {

    @Query("SELECT new f5.health.app.dto.device.DeviceAndMemberRole(d, m.role)" +
            " FROM Device d" +
            " JOIN d.deviceId.member m" +
            " WHERE d.refreshToken = :refreshToken")
    Optional<DeviceAndMemberRole> findDeviceAndMemberRoleByRefreshToken(@Param("refreshToken") String refreshToken);


    void deleteByDeviceIdMemberIdAndRefreshToken(Long memberId, String refreshToken);
}
