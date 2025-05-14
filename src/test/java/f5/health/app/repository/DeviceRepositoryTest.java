package f5.health.app.repository;

import f5.health.app.constant.System;
import f5.health.app.entity.Device.Device;
import f5.health.app.entity.Device.DeviceId;
import f5.health.app.entity.Member;
import f5.health.app.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import f5.health.app.HealthApplication;
import f5.health.app.repository.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest(classes = HealthApplication.class)
@ActiveProfiles("test")
public class DeviceRepositoryTest {

    DeviceId deviceId;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        // 멤버 저장
        Member member = Member.createMember(
                "oauth123",
                "deviceuser@example.com",
                "디바이스유저",
                f5.health.app.constant.Role.USER,
                new Member.MemberCheckUp(
                        LocalDate.of(1990, 1, 1),
                        f5.health.app.constant.Gender.남자,
                        170, 65,
                        f5.health.app.constant.BloodType.O,
                        0, 2, 3
                )
        );
        memberRepository.save(member);

        // 가짜 RefreshToken 객체 생성
        JwtProvider jwtProvider = new JwtProvider("thisIsASecretKeyThatIsLongEnough1234");
        JwtProvider.RefreshToken refreshToken = jwtProvider.issueRefreshToken(1L);

        // 디바이스 저장
        Device device = Device.of(member, "test-udid", System.iOS, refreshToken);
        deviceRepository.save(device);

        this.deviceId = new DeviceId(member, "test-udid");
    }

    @Test
    void 디바이스_조회() {
        Optional<Device> found = deviceRepository.findById(deviceId);
        assertTrue(found.isPresent());
        assertEquals("test-udid", found.get().getDeviceId().getUdid());
    }

    @Test
    void 디바이스_삭제() {
        assertTrue(deviceRepository.existsById(deviceId));
        deviceRepository.deleteById(deviceId);
        assertFalse(deviceRepository.existsById(deviceId));
    }

    @Test
    void 디바이스_수정() {
        Device device = deviceRepository.findById(deviceId).orElseThrow();
        String newToken = "updated-token";
        Date newExpiration = new Date(java.lang.System.currentTimeMillis() + 9999999);

        // JwtProvider 인스턴스를 먼저 생성
        JwtProvider jwtProvider = new JwtProvider("thisIsASecretKeyThatIsLongEnough1234");

        // 내부 클래스가 아니라 메서드로 발급 (권장 방식)
        JwtProvider.RefreshToken refreshToken = jwtProvider.issueRefreshToken(device.getDeviceId().getMember().getId());

        // 새로운 디바이스 생성
        Device updated = Device.of(device.getDeviceId().getMember(), device.getDeviceId().getUdid(), System.iOS, refreshToken);
        deviceRepository.save(updated);

        Device result = deviceRepository.findById(deviceId).orElseThrow();
        assertEquals(refreshToken.getValue(), result.getRefreshToken());
        assertEquals(System.iOS, result.getOs());
    }

}
