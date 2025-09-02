package f5.health.app.service;

import f5.health.app.member.constant.Role;
import f5.health.app.member.entity.Member;
import f5.health.app.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

    @Mock
    MemberRepository memberRepository;


    @Test
    void save() {
        Member member = createMember(4, 500, 3);
        System.out.println(member);
        System.out.println(member.getId());

    }


    private Member createMember(int smokingCigarettesPerDay, int weekAlcoholIntake, int weekExerciseFrequency) {
        Member.CheckUp memberCheckUp = Member.CheckUp.builder()
                .height(177)
                .weight(60)
                .build();
        Member member = Member.createMember("OAuthId", "email", "nickname", Role.USER, memberCheckUp);

        when(memberRepository.save(any(Member.class))).thenReturn(member);
        return memberRepository.save(member);
    }
}
