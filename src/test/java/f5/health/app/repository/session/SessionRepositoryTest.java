package f5.health.app.repository.session;

import f5.health.app.member.constant.Role;
import f5.health.app.session.entity.Session;
import f5.health.app.member.entity.Member;
import f5.health.app.jwt.JwtProvider;
import f5.health.app.member.repository.MemberRepository;
import f5.health.app.session.repository.SessionRepository;
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

    
    @DisplayName("회원 아이디로 세션 목록 조회")
    @Test
    void findByMemberId() {
        Member member = createMember();
        JwtProvider.RefreshToken refreshToken = tokenProvider.issueRefreshToken(member.getId());
        createSession(member, refreshToken);

        List<Session> sessions = this.sessionRepository.findByMemberIdJoinFetch(member.getId());

        assertThat(sessions.size()).isPositive();
        assertThat(sessions).allMatch(session -> session.getMember().getId().equals(member.getId()));
        assertThat(sessions).allMatch(session -> session.getRefreshToken().equals(refreshToken.value()));
        assertThat(sessions).allMatch(session -> session.getRefreshTokenExpiration().equals(refreshToken.getExpiration()));
    }

    @DisplayName("해당 갱신 토큰이 저장된 세션을 조회해서 갱신 토큰 교체")
    @Test
    void findByRefreshToken() {
        Member member = createMember();
        JwtProvider.RefreshToken refreshToken = tokenProvider.issueRefreshToken(member.getId());
        createSession(member, refreshToken);

        Session findSession = this.sessionRepository.findByRefreshTokenJoinFetch(refreshToken.value()).orElseThrow();
        JwtProvider.RefreshToken reissuedRefreshToken = tokenProvider.issueRefreshToken(findSession.getMember().getId());
        findSession.rotateRefreshToken(reissuedRefreshToken);

        assertThat(findSession.getRefreshToken()).isEqualTo(reissuedRefreshToken.value());
        assertThat(findSession.getRefreshTokenExpiration()).isEqualTo(reissuedRefreshToken.getExpiration());
    }


    private Member createMember() {
        Member.CheckUp memberCheckUp = Member.CheckUp.builder().build();
        Member member = Member.createMember("OAuthId", "email", "nickname", Role.USER, memberCheckUp);
        memberRepository.save(member);
        return member;
    }

    private Session createSession(Member member, JwtProvider.RefreshToken refreshToken) {
        Session session = Session.of(member, "7946DA4E-8429-423C-B405-B3FC77914E3E", iOS, refreshToken);
        sessionRepository.save(session);
        return session;
    }
}
