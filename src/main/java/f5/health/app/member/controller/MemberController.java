package f5.health.app.member.controller;

import f5.health.app.auth.Login;
import f5.health.app.common.EnumModel;
import f5.health.app.common.EnumModelMapper;
import f5.health.app.member.constant.Badge;
import f5.health.app.member.service.MemberUpdateRequest;
import f5.health.app.auth.vo.LoginMember;
import f5.health.app.member.service.MemberService;
import f5.health.app.member.vo.MemberProfile;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController implements MemberApiDocs {

    private final EnumModelMapper enumMapper;
    private final MemberService memberService;

    @GetMapping("/badges")
    public List<? extends EnumModel> badges() {
        return enumMapper.get(Badge.class);
    }

    @GetMapping("/me")
    public MemberProfile profile(@Login LoginMember loginMember) {
        return memberService.getMyProfile(loginMember);
    }

    @PatchMapping("/me/edit")
    public void updaaye(@Login LoginMember loginMember, @RequestBody @Valid MemberUpdateRequest updateParam) {
        memberService.updateMember(loginMember, updateParam);
    }
}
