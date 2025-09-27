package f5.health.app.member.service;

import f5.health.app.auth.jwt.vo.JwtMember;
import f5.health.app.common.exception.NotFoundException;
import f5.health.app.member.constant.Role;
import f5.health.app.member.entity.Member;
import f5.health.app.member.entity.vo.MemberCheckUp;
import f5.health.app.member.exception.MemberAlreadyJoinedException;
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

    private final MemberRepository memberRepository;

    /** 내 정보 */
    public MemberProfile getMyProfile(JwtMember loginMember) {
        Member member = this.findById(loginMember.id());
        return new MemberProfile(member);
    }

    @Transactional
    public void updateMember(JwtMember loginMember, MemberUpdateRequest updateParam) {
        Member member = memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));

        member.updateProfile(updateParam.nickname(), updateParam.height(), updateParam.weight());
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER, memberId.toString()));
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    /** 회원가입 */
    public Long join(OAuth2UserInfo userInfo, MemberCheckUp checkUp) {
        this.validateDuplicateMember(userInfo.getEmail());
        Member member = Member.createMember()
                .oauthId(userInfo.getOAuthId())
                .email(userInfo.getEmail())
                .nickname(userInfo.getNickname())
                .role(Role.USER)
                .checkUp(checkUp)
                .build();
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
