package f5.health.app.session.service;

import f5.health.app.session.entity.Session;
import f5.health.app.member.entity.Member;
import f5.health.app.auth.jwt.JwtProvider;
import f5.health.app.session.repository.SessionRepository;
import f5.health.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final MemberService memberService;
    private final SessionRepository sessionRepository;

    @Transactional
    public void save(Long memberId, DeviceInfo deviceInfo, JwtProvider.RefreshToken refreshToken) {
        Member member = memberService.findById(memberId);
        sessionRepository.save(Session.of(member, deviceInfo.udid(), deviceInfo.os(), refreshToken));
    }

    /** 토큰 재발급 시 사용 */
    public Optional<Session> findByRefreshTokenJoinFetch(String refreshToken) {
        return sessionRepository.findByRefreshTokenJoinFetch(refreshToken);
    }
}
