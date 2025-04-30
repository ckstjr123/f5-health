package f5.health.app.service.member;

import f5.health.app.constant.Role;
import f5.health.app.entity.Member;
import f5.health.app.repository.MemberRepository;
import f5.health.app.service.auth.vo.oauth2userinfo.OAuth2UserInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
//@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public Member join(OAuth2UserInfo userInfo, Member.MemberCheckUp memberCheckUp) {
        Member joinMember = Member.createMember(userInfo.getOAuthId(), userInfo.getEmail(), userInfo.getNickname(), Role.USER, memberCheckUp);
        return this.memberRepository.save(joinMember);
    }

    public Optional<Member> findByEmail(String email) {
        return this.memberRepository.findByEmail(email);
    }

}
