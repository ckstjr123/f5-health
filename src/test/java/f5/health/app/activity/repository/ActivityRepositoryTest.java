package f5.health.app.activity.repository;

import f5.health.app.activity.entity.Activity;
import f5.health.app.member.entity.Member;
import f5.health.app.member.fixture.MemberFixture;
import f5.health.app.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ActivityRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    void findByMemberIdAndRecordDate() {
        Member member = memberRepository.save(MemberFixture.createMember());
        LocalDate recordDate = LocalDate.now();
        Activity activity = Activity.builder(member)
                .waterIntake(500)
                .recordDate(recordDate)
                .build();
        activityRepository.save(activity);

        Activity findActivity = activityRepository.findByMemberIdAndRecordDate(member.getId(), recordDate).orElseThrow();

        assertThat(findActivity).isEqualTo(activity);
    }

}
