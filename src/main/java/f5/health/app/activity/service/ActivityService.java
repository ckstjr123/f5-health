package f5.health.app.activity.service;

import f5.health.app.activity.domain.Activity;
import f5.health.app.activity.domain.alcoholconsumption.AlcoholConsumption;
import f5.health.app.activity.domain.alcoholconsumption.AlcoholConsumptionFactory;
import f5.health.app.activity.repository.ActivityRepository;
import f5.health.app.activity.vo.ActivityResponse;
import f5.health.app.common.exception.ConflictException;
import f5.health.app.common.exception.NotFoundException;
import f5.health.app.member.entity.Member;
import f5.health.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static f5.health.app.activity.constant.ActivityErrorCode.DUPLICATED_ACTIVITY;
import static f5.health.app.activity.constant.ActivityErrorCode.NOT_FOUND_ACTIVITY;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final MemberService memberService;
    private final ActivityRepository activityRepository;

    @Transactional
    public ActivityResponse findActivity(Long memberId, LocalDate recordDate) {
        Activity activity = activityRepository.findByMemberIdAndRecordDate(memberId, recordDate)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACTIVITY));
        return ActivityResponse.from(activity);
    }

    @Transactional
    public Long save(Long memberId, ActivityRequest activityRequest) {
        Member member = memberService.findById(memberId);
        validateDuplicateActivity(memberId, activityRequest.recordDate());

        List<AlcoholConsumption> alcoholConsumptions = AlcoholConsumptionFactory.from(activityRequest.alcoholParams());
        Activity activity = Activity.createActivity(member, alcoholConsumptions)
                .waterIntake(activityRequest.waterIntake())
                .smokedCigarettes(activityRequest.smokedCigarettes())
                .recordDate(activityRequest.recordDate())
                .build();
        activityRepository.save(activity);
        return activity.getId();
    }

    private void validateDuplicateActivity(Long memberId, LocalDate recordDate) {
        Optional<Activity> findActivity = activityRepository.findByMemberIdAndRecordDate(memberId, recordDate);
        if (findActivity.isPresent()) {
            throw new ConflictException(DUPLICATED_ACTIVITY);
        }
    }
}
