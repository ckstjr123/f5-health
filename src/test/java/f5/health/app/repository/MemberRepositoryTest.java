package f5.health.app.repository;

import f5.health.app.HealthApplication;
import f5.health.app.constant.BloodType;
import f5.health.app.constant.Gender;
import f5.health.app.constant.Role;
import f5.health.app.entity.Member;
import f5.health.app.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = HealthApplication.class)
@Transactional // 테스트 후 자동 롤백
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    Member savedMember;

    @BeforeEach
    void setup() {
        Member.MemberCheckUp checkUp = new Member.MemberCheckUp(
                LocalDate.of(1990, 1, 1),
                Gender.남자,
                175, 70,
                BloodType.A,
                0, 3, 4
        );

        savedMember = Member.createMember(
                "oauth123",
                "test1@example.com",
                "홍길동",
                Role.USER,
                checkUp
        );

        memberRepository.save(savedMember);
    }

    @Test
    void 이메일로_회원_조회() {
        System.out.println("이메일 조회 테스트 실행");

        Optional<Member> found = memberRepository.findByEmail("test1@example.com");

        System.out.println("조회 결과: "+ found);
        assertTrue(found.isPresent());
        assertEquals("홍길동", found.get().getNickname());
    }

    @Test
    void 없는_이메일은_조회되지_않음() {
        System.out.println("없는 이메일 조회 테스트 실행");
        Optional<Member> found = memberRepository.findByEmail("notfound@example.com");

        assertTrue(found.isEmpty());
    }
}