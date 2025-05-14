package f5.health.app.entity.Device;

import f5.health.app.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** JPA 복합키 (`MEMBER_ID`, UDID) 매핑 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceId {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "UDID", length = 36)
    private String udid; // iOS identifierForVendor

    public DeviceId(Member member, String udid) {
        this.member = member;
        this.udid = udid;
    }
}
