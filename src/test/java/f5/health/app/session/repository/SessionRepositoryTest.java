package f5.health.app.session.repository;

import f5.health.app.auth.jwt.JwtProvider;
import f5.health.app.member.entity.Member;
import f5.health.app.member.fixture.MemberFixture;
import f5.health.app.member.repository.MemberRepository;
import f5.health.app.session.entity.Session;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static f5.health.app.session.constant.System.iOS;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
public class SessionRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SessionRepository sessionRepository;

    private final JwtProvider tokenProvider = new JwtProvider("vmfhaqwfek2ok1314lkedqwlwde2ldl2");

    @DisplayName("세션 저장")
    @Test
    void save() {
        Member member = memberRepository.save(MemberFixture.createMember());
        JwtProvider.RefreshToken refreshToken = tokenProvider.issueRefreshToken(member.getId());
        Session session = Session.of(member, "7946DA4E-8429-423C-B405-B3FC77914E3E", iOS, refreshToken.value(), refreshToken.getExpiration());

        sessionRepository.save(session);

        assertThat(sessionRepository.findById(session.getId()).orElseThrow()).isEqualTo(session);
    }

    @DisplayName("회원 아이디로 세션 목록 조회")
    @Test
    void findByMemberId() {
        Member member = memberRepository.save(MemberFixture.createMember());
        JwtProvider.RefreshToken refreshToken = tokenProvider.issueRefreshToken(member.getId());
        List<Session> sessions = sessionRepository.saveAll(
                List.of(Session.of(member, "7946DA4E-8429-423C-B405-B3FC77914E3E", iOS, refreshToken.value(), refreshToken.getExpiration()))
        );

        List<Session> findSessions = this.sessionRepository.findByMemberIdJoinFetch(member.getId());

        assertThat(findSessions).containsExactlyElementsOf(sessions);
    }

    @DisplayName("해당 갱신 토큰이 저장된 세션을 조회해서 갱신 토큰 교체")
    @Test
    void findByRefreshToken() {
        Member member = memberRepository.save(MemberFixture.createMember());
        JwtProvider.RefreshToken refreshToken = tokenProvider.issueRefreshToken(member.getId());
        sessionRepository.save(Session.of(member, "7946DA4E-8429-423C-B405-B3FC77914E3E", iOS, refreshToken.value(), refreshToken.getExpiration()));

        Session findSession = this.sessionRepository.findByRefreshTokenJoinFetch(refreshToken.value()).orElseThrow();
        JwtProvider.RefreshToken reissuedRefreshToken = tokenProvider.issueRefreshToken(findSession.getMember().getId());
        findSession.rotateRefreshToken(reissuedRefreshToken.value(), reissuedRefreshToken.getExpiration());

        assertThat(findSession.getRefreshToken()).isEqualTo(reissuedRefreshToken.value());
        assertThat(findSession.getRefreshTokenExpiration()).isEqualTo(reissuedRefreshToken.getExpiration());
    }

}
