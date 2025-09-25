package f5.health.app.member.service;

import f5.health.app.common.EnumModelMapper;
import f5.health.app.member.constant.Role;
import f5.health.app.member.entity.Member;
import f5.health.app.common.exception.NotFoundException;
import f5.health.app.member.exception.MemberAlreadyJoinedException;
import f5.health.app.auth.jwt.vo.JwtMember;
import f5.health.app.member.repository.MemberRepository;
import f5.health.app.member.service.oauth2userinfo.OAuth2UserInfo;
import f5.health.app.member.vo.MemberProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static f5.health.app.member.exception.MemberErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final EnumModelMapper enumMapper;
    private final MemberRepository memberRepository;

    /** 내 정보 */
    public MemberProfile getMyProfile(JwtMember loginMember) {
        Member member = this.findById(loginMember.getId());
        return new MemberProfile(member, enumMapper);
    }

    @Transactional
    public void updateMember(JwtMember loginMember, MemberUpdateRequest updateParam) {
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));

        member.updateProfile(updateParam.getNickname(), updateParam.getHeight(), updateParam.getWeight());
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER, memberId.toString()));
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    /** 회원가입 */
    public Long join(OAuth2UserInfo userInfo, Member.CheckUp memberCheckUp) {
        this.validateDuplicateMember(userInfo.getEmail());
        Member member = Member.createMember(userInfo.getOAuthId(), userInfo.getEmail(), userInfo.getNickname(), Role.USER, memberCheckUp);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (!findMember.isEmpty()) {
            throw new MemberAlreadyJoinedException();
        }
    }
    
}
