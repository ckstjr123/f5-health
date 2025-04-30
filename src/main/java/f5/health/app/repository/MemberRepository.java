package f5.health.app.repository;

import f5.health.app.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // CRUD...
    Optional<Member> findByEmail(String email); // email(unique)
}
