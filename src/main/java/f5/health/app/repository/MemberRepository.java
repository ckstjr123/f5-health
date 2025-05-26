package f5.health.app.repository;

import f5.health.app.entity.member.Member;
import f5.health.app.vo.member.response.MemberSavings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email); // email(unique)

    Optional<MemberSavings> getMemberSavingsById(Long memberId);
}
