package f5.health.app.member.fixture;

import f5.health.app.member.constant.Role;
import f5.health.app.member.entity.Member;
import f5.health.app.member.entity.vo.MemberCheckUp;

import java.util.UUID;

public class MemberFixture {

    public static Member createMember() {
        String uuid = UUID.randomUUID().toString();
        MemberCheckUp checkUp = MemberCheckUp.builder()
                .height(177.5)
                .weight(60.55)
                .build();

        return Member.createMember()
                .oauthId(uuid)
                .email(uuid + "@email.com")
                .nickname("nickname")
                .role(Role.USER)
                .checkUp(checkUp)
                .build();
    }
}
