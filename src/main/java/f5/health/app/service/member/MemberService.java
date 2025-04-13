package f5.health.app.service.member;

import f5.health.app.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
}
