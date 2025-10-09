package f5.health.app.activity.repository;

import f5.health.app.activity.constant.AlcoholType;
import f5.health.app.activity.domain.Activity;
import f5.health.app.activity.domain.alcoholconsumption.AlcoholConsumption;
import f5.health.app.activity.domain.alcoholconsumption.AlcoholConsumptionFactory;
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
        List<AlcoholConsumption> alcoholConsumptions = List.of(AlcoholConsumptionFactory.of(AlcoholType.BEER, 500));
        LocalDate recordDate = LocalDate.now();
        Activity activity = Activity.createActivity(member, alcoholConsumptions)
                .waterIntake(500)
                .recordDate(recordDate)
                .build();
        activityRepository.save(activity);

        Activity findActivity = activityRepository.findByMemberIdAndRecordDate(member.getId(), recordDate).orElseThrow();

        assertThat(findActivity).isEqualTo(activity);
        assertThat(findActivity.getAlcoholConsumptions()).containsExactlyElementsOf(alcoholConsumptions);
    }

}
