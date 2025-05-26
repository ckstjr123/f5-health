package f5.health.app.service.member;

import f5.health.app.constant.EnumModelMapper;
import f5.health.app.constant.member.Role;
import f5.health.app.entity.member.Member;
import f5.health.app.entity.member.MemberUpdateRequest;
import f5.health.app.exception.global.NotFoundException;
import f5.health.app.exception.member.MemberAlreadyJoinedException;
import f5.health.app.jwt.JwtMember;
import f5.health.app.repository.MemberRepository;
import f5.health.app.service.auth.vo.oauth2userinfo.OAuth2UserInfo;
import f5.health.app.vo.member.response.MemberProfile;
import f5.health.app.vo.member.response.MemberSavings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static f5.health.app.exception.member.MemberErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
//@Transactional
public class MemberService {

    private final EnumModelMapper enumMapper;
    private final MemberRepository memberRepository;

    /** 내 정보 */
    public MemberProfile getMyProfile(JwtMember loginMember) {
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));
        return new MemberProfile(member, enumMapper);
    }

    @Transactional
    public void updateMember(JwtMember loginMember, MemberUpdateRequest updateParam) {
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));

        member.updateProfile(updateParam); //
    }

    /** 회원 절약 금액 관련 데이터 */
    public MemberSavings getMemberSavings(JwtMember loginMember) {
        return memberRepository.getMemberSavingsById(loginMember.getId()).orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    /** 회원 중복 체크 후 회원가입 처리 */
    public Member join(OAuth2UserInfo userInfo, Member.MemberCheckUp memberCheckUp) {
        this.validateDuplicateMember(userInfo.getEmail());
        Member joinMember = Member.createMember(userInfo.getOAuthId(), userInfo.getEmail(), userInfo.getNickname(), Role.USER, memberCheckUp);
        return memberRepository.save(joinMember);
    }

    private void validateDuplicateMember(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (!findMember.isEmpty()) {
            throw new MemberAlreadyJoinedException();
        }
    }
    
}
