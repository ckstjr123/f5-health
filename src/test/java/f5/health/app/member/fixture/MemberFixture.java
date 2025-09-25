package f5.health.app.member.fixture;

import f5.health.app.member.constant.Role;
import f5.health.app.member.entity.Member;

import java.util.UUID;

public class MemberFixture {

    public static Member createMember() {
        String uuid = UUID.randomUUID().toString();
        Member.CheckUp memberCheckUp = Member.CheckUp.builder()
                .height(177)
                .weight(60)
                .build();
        return Member.createMember(uuid, uuid + "@email.com", "nickname", Role.USER, memberCheckUp);
    }
}
