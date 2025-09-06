package f5.health.app.member.controller;

import f5.health.app.member.service.MemberUpdateRequest;
import f5.health.app.auth.jwt.vo.JwtMember;
import f5.health.app.member.service.MemberService;
import f5.health.app.member.vo.MemberProfile;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController implements MemberApiDocs {

    private final MemberService memberService;

    @GetMapping("/me")
    public MemberProfile profile(@AuthenticationPrincipal JwtMember loginMember) {
        return memberService.getMyProfile(loginMember);
    }

    @PatchMapping("/me/edit")
    public void edit(@AuthenticationPrincipal JwtMember loginMember,
                     @RequestBody @Valid MemberUpdateRequest updateParam) {
        memberService.updateMember(loginMember, updateParam);
    }
}
