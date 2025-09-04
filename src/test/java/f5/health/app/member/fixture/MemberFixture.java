package f5.health.app.member.fixture;

import f5.health.app.member.constant.Role;
import f5.health.app.member.entity.Member;

public class MemberFixture {

    public static Member createMember() {
        Member.CheckUp memberCheckUp = Member.CheckUp.builder()
                .height(177)
                .weight(60)
                .build();
        return Member.createMember("OAuthId", "abc123@gmail.com", "nickname", Role.USER, memberCheckUp);
    }
}
