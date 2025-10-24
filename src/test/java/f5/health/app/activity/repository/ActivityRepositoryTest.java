package f5.health.app.activity.repository;

import f5.health.app.activity.domain.Activity;
import f5.health.app.activity.domain.AlcoholType;
import f5.health.app.activity.vo.ActivityRequest;
import f5.health.app.member.entity.Member;
import f5.health.app.member.fixture.MemberFixture;
import f5.health.app.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ActivityRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    void findByMemberIdAndRecordedDate() {
        Member member = memberRepository.save(MemberFixture.createMember());
        ActivityRequest.AlcoholConsumptionParam alcoholParam = new ActivityRequest.AlcoholConsumptionParam(AlcoholType.SOJU, 500);
        LocalDate recordedDate = LocalDate.now();
        Activity activity = Activity.createActivity(member, List.of(alcoholParam))
                .waterIntake(500)
                .recordedDate(recordedDate)
                .build();
        activityRepository.save(activity);

        Activity findActivity = activityRepository.findByMemberIdAndRecordedDate(member.getId(), recordedDate).orElseThrow();

        assertThat(findActivity).isEqualTo(activity);
        assertThat(findActivity.getAlcoholConsumptions()).containsExactlyElementsOf(activity.getAlcoholConsumptions());
    }

    @Test
    void findOneJoinFetch() {
        Member member = memberRepository.save(MemberFixture.createMember());
        ActivityRequest.AlcoholConsumptionParam alcoholParam = new ActivityRequest.AlcoholConsumptionParam(AlcoholType.BEER, 500);
        LocalDate recordedDate = LocalDate.now();
        Activity activity = Activity.createActivity(member, List.of(alcoholParam))
                .smokedCigarettes(3)
                .recordedDate(recordedDate)
                .build();
        activityRepository.save(activity);

        Activity findActivity = activityRepository.findActivityJoinFetch(member.getId(), recordedDate).orElseThrow();

        assertThat(findActivity).isEqualTo(activity);
        assertThat(findActivity.getAlcoholConsumptions()).containsExactlyElementsOf(activity.getAlcoholConsumptions());
    }

}
