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
    void findByMemberIdAndRecordDate() {
        Member member = memberRepository.save(MemberFixture.createMember());
        ActivityRequest.AlcoholConsumptionParam alcoholParam = new ActivityRequest.AlcoholConsumptionParam(AlcoholType.SOJU, 500);
        LocalDate recordDate = LocalDate.now();
        Activity activity = Activity.createActivity(member, List.of(alcoholParam))
                .waterIntake(500)
                .recordDate(recordDate)
                .build();
        activityRepository.save(activity);

        Activity findActivity = activityRepository.findByMemberIdAndRecordDate(member.getId(), recordDate).orElseThrow();

        assertThat(findActivity).isEqualTo(activity);
        assertThat(findActivity.getAlcoholConsumptions()).containsExactlyElementsOf(activity.getAlcoholConsumptions());
    }

    @Test
    void findOneJoinFetch() {
        Member member = memberRepository.save(MemberFixture.createMember());
        ActivityRequest.AlcoholConsumptionParam alcoholParam = new ActivityRequest.AlcoholConsumptionParam(AlcoholType.BEER, 500);
        LocalDate recordDate = LocalDate.now();
        Activity activity = Activity.createActivity(member, List.of(alcoholParam))
                .smokedCigarettes(3)
                .recordDate(recordDate)
                .build();
        activityRepository.save(activity);

        Activity findActivity = activityRepository.findActivityJoinFetch(member.getId(), recordDate).orElseThrow();

        assertThat(findActivity).isEqualTo(activity);
        assertThat(findActivity.getAlcoholConsumptions()).containsExactlyElementsOf(activity.getAlcoholConsumptions());
    }

}
