package f5.health.app.repository.device;

import f5.health.app.constant.member.BloodType;
import f5.health.app.constant.member.Gender;
import f5.health.app.constant.member.Role;
import f5.health.app.entity.Device.Device;
import f5.health.app.entity.Member;
import f5.health.app.jwt.JwtProvider;
import f5.health.app.repository.DeviceRepository;
import f5.health.app.repository.MemberRepository;
import f5.health.app.vo.device.DeviceAndMemberRole;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static f5.health.app.constant.device.System.iOS;
import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DataJpaTest
public class DeviceRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    private final JwtProvider tokenProvider = new JwtProvider("vmfhaqwfek2ok1314lkedqwlwde2ldl2");


    @DisplayName("해당 갱신 토큰이 저장된 Device와 Member Role을 조회한다.")
    @Test
    void findDeviceAndMemberRoleByRefreshToken() {
        Member member = joinMember();
        JwtProvider.RefreshToken refreshToken = tokenProvider.issueRefreshToken(member.getId());
        registerDevice(member, refreshToken);

        em.flush();
        em.clear();

        DeviceAndMemberRole deviceAndRole = this.deviceRepository.findDeviceAndMemberRoleByRefreshToken(refreshToken.value()).orElseThrow();
        Device refreshDevice = deviceAndRole.getDevice();
        JwtProvider.RefreshToken reissuedRefreshToken = tokenProvider.issueRefreshToken(member.getId());
        refreshDevice.rotateRefreshToken(reissuedRefreshToken);
        Member refreshMember = refreshDevice.getDeviceId().getMember();

        assertThat(refreshMember.getId()).isEqualTo(member.getId());
        assertThat(refreshMember).isInstanceOf(HibernateProxy.class);
        assertThat(refreshDevice.getRefreshToken()).isEqualTo(reissuedRefreshToken.value());
        assertThat(refreshDevice.getRefreshTokenExpiration()).isEqualTo(reissuedRefreshToken.getExpiration());
        assertThat(deviceAndRole.getMemberRole()).isEqualTo(member.getRole().name());
    }

    @DisplayName("로그아웃된 기기를 삭제한다.")
    @Test
    void deleteByMemberIdAndRefreshToken() {
        Member logoutMember = joinMember();
        JwtProvider.RefreshToken refreshToken = tokenProvider.issueRefreshToken(logoutMember.getId());
        Device logoutDevice = registerDevice(logoutMember, refreshToken);

        this.deviceRepository.deleteByDeviceIdMemberIdAndRefreshToken(logoutMember.getId(), logoutDevice.getRefreshToken());
        em.flush(); em.clear();

        assertThat(this.deviceRepository.findById(logoutDevice.getDeviceId())).isEmpty();
    }

    private Member joinMember() {
        Member member = Member.createMember("OAuthId", "email", "nickname", Role.USER,
                new Member.MemberCheckUp(LocalDate.now(), Gender.MALE, 173, 60, BloodType.B, 15, 22, 33));
        memberRepository.save(member);
        return member;
    }

    private Device registerDevice(Member member, JwtProvider.RefreshToken refreshToken) {
        Device device = Device.of(member, "7946DA4E-8429-423C-B405-B3FC77914E3E", iOS, refreshToken);
        deviceRepository.save(device);
        return device;
    }
}
