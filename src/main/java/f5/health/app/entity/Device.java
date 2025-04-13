package f5.health.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "DEVICE", uniqueConstraints = @UniqueConstraint(columnNames = {"UDID", "MEMBER_ID"}))
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEVICE_ID")
    private Long id;

    // 복합 고유키 (UDID, MEMBER_ID) //
    @Column(name = "UDID")
    private String udid; // IOS(identifierForVendor)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;
    
    @Column(name = "REFRESH_TOKEN_EXP")
    private LocalDateTime refreshTokenExpiration;
}
