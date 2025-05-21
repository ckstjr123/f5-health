package f5.health.app.controller.member;

import f5.health.app.jwt.JwtMember;
import f5.health.app.service.member.MemberService;
import f5.health.app.vo.member.response.MemberProfile;
import f5.health.app.vo.member.response.MemberSavings;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController implements MemberApiDocs {

    private final MemberService memberService;

    @GetMapping("/me")
    public MemberProfile getProfile(@AuthenticationPrincipal JwtMember loginMember) {
        return memberService.getMyProfile(loginMember);
    }

    @GetMapping("/me/savings")
    public MemberSavings savings(@AuthenticationPrincipal JwtMember loginMember) {
        return memberService.getMemberSavings(loginMember);
    }

}
