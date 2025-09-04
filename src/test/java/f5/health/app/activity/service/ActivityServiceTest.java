package f5.health.app.activity.service;

import f5.health.app.activity.repository.ActivityRepository;
import f5.health.app.member.fixture.MemberFixture;
import f5.health.app.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

    @Mock
    private MemberService memberService;

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityService activityService;


//    @Test
//    void save() {
//    }

}
