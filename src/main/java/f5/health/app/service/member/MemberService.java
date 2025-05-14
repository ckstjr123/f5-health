package f5.health.app.service.member;

import f5.health.app.constant.member.Role;
import f5.health.app.entity.Member;
import f5.health.app.exception.member.MemberAlreadyJoinedException;
import f5.health.app.repository.MemberRepository;
import f5.health.app.service.auth.vo.oauth2userinfo.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
//@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    /** 회원 중복 체크 후 회원가입 처리 */
    public Member join(OAuth2UserInfo userInfo, Member.MemberCheckUp memberCheckUp) {
        this.validateDuplicateMember(userInfo.getEmail());
        Member joinMember = Member.createMember(userInfo.getOAuthId(), userInfo.getEmail(), userInfo.getNickname(), Role.USER, memberCheckUp);
        return memberRepository.save(joinMember);
    }

    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }


    private void validateDuplicateMember(String email) {
        Optional<Member> findMember = this.memberRepository.findByEmail(email);
        if (!findMember.isEmpty()) {
            throw new MemberAlreadyJoinedException();
        }
    }
}
