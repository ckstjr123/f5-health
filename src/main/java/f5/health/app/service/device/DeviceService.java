package f5.health.app.service.device;

import f5.health.app.entity.device.Device;
import f5.health.app.entity.member.Member;
import f5.health.app.exception.global.NotFoundException;
import f5.health.app.jwt.JwtProvider;
import f5.health.app.repository.DeviceRepository;
import f5.health.app.service.auth.vo.DeviceInfo;
import f5.health.app.service.member.MemberService;
import f5.health.app.vo.device.DeviceAndMemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static f5.health.app.exception.member.MemberErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final MemberService memberService;
    private final DeviceRepository deviceRepository;

    /** 갱신 토큰과 함께 접속 유저 기기 등록(동일한 식별자를 가진 기존 디바이스가 있으면 병합되고 없으면 새로 저장됨) */
    @Transactional
    public void registerDevice(Long memberId, DeviceInfo deviceInfo, JwtProvider.RefreshToken refreshToken) {
        Member member = memberService.findById(memberId);
        // spring data jpa repository save(S entity): persist or merge
        deviceRepository.save(Device.of(member, deviceInfo.getUdid(), deviceInfo.getOs(), refreshToken));
    }

    /** 토큰 재발급 시 사용 */
    public Optional<DeviceAndMemberRole> findDeviceAndMemberRoleBy(String refreshToken) {
        return deviceRepository.findDeviceAndMemberRoleByRefreshToken(refreshToken);
    }

    /** 로그아웃 시 사용 */
    public void deleteByMemberIdAndRefreshToken(Long memberId, String refreshToken) {
        deviceRepository.deleteByDeviceIdMemberIdAndRefreshToken(memberId, refreshToken);
    }

}
