package f5.health.app.member.repository;

import f5.health.app.member.entity.Member;
import f5.health.app.member.fixture.MemberFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void findByEmail() {
        Member member = memberRepository.save(MemberFixture.createMember());

        Member findMember = memberRepository.findByEmail(member.getEmail()).orElseThrow();

        assertThat(findMember).isEqualTo(member);
    }

}
