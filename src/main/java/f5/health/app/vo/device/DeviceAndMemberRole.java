package f5.health.app.vo.device;

import f5.health.app.constant.member.Role;
import f5.health.app.entity.device.Device;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeviceAndMemberRole {

    @Getter
    private final Device device;
    private final Role memberRole;

    public String getMemberRole() {
        return this.memberRole.name();
    }
}
