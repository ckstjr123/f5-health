package f5.health.app.session.entity;

import f5.health.app.jwt.JwtProvider;
import f5.health.app.member.entity.Member;
import f5.health.app.session.constant.System;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "SESSION")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SESSION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "UDID", length = 36)
    private String udid; // iOS identifierForVendor

    @Column(name = "OS")
    @Enumerated(EnumType.STRING)
    private System os;

    @Column(name = "REFRESH_TOKEN", unique = true)
    private String refreshToken;
    
    @Column(name = "REFRESH_TOKEN_EXP")
    private Date refreshTokenExpiration;

    public static Session of(Member member, String udid, System os, JwtProvider.RefreshToken refreshToken) {
        Session session = new Session();
        session.member = member;
        session.udid = udid;
        session.os = os;
        session.refreshToken = refreshToken.value();
        session.refreshTokenExpiration = refreshToken.getExpiration();
        return session;
    }


    public void rotateRefreshToken(JwtProvider.RefreshToken refreshToken) {
        this.refreshToken = refreshToken.value();
        this.refreshTokenExpiration = refreshToken.getExpiration();
    }
}
