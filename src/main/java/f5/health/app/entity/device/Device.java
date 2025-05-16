package f5.health.app.entity.device;

import f5.health.app.constant.device.System;
import f5.health.app.entity.Member;
import f5.health.app.jwt.JwtProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "DEVICE")
public class Device {

    @EmbeddedId
    private DeviceId deviceId; // primary key (`MEMBER_ID`, UDID)

    @Column(name = "OS")
    @Enumerated(EnumType.STRING)
    private System os;

    @Column(name = "REFRESH_TOKEN", unique = true)
    private String refreshToken;
    
    @Column(name = "REFRESH_TOKEN_EXP")
    private Date refreshTokenExpiration;

    public static Device of(Member member, String udid, System os, JwtProvider.RefreshToken refreshToken) {
        Device device = new Device();
        device.deviceId = new DeviceId(member, udid);
        device.os = os;
        device.refreshToken = refreshToken.value();
        device.refreshTokenExpiration = refreshToken.getExpiration();
        return device;
    }


    public void rotateRefreshToken(JwtProvider.RefreshToken refreshToken) {
        this.refreshToken = refreshToken.value();
        this.refreshTokenExpiration = refreshToken.getExpiration();
    }
}
