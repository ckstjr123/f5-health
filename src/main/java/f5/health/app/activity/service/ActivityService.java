package f5.health.app.activity.service;

import f5.health.app.activity.entity.Activity;
import f5.health.app.member.entity.Member;
import f5.health.app.common.exception.DuplicateEntityException;
import f5.health.app.common.exception.NotFoundException;
import f5.health.app.activity.repository.ActivityRepository;
import f5.health.app.activity.service.request.ActivityRequest;
import f5.health.app.member.service.MemberService;
import f5.health.app.activity.vo.ActivityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static f5.health.app.activity.constant.ActivityErrorCode.DUPLICATED_ACTIVITY;
import static f5.health.app.activity.constant.ActivityErrorCode.NOT_FOUND_ACTIVITY;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final MemberService memberService;
    private final ActivityRepository activityRepository;

    @Transactional(readOnly = true)
    public ActivityResponse findActivity(Long memberId, LocalDate recordDate) {
        Activity activity = activityRepository.findByMemberIdAndRecordDate(memberId, recordDate)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACTIVITY));
        return new ActivityResponse(activity);
    }

    @Transactional
    public Long save(Long memberId, ActivityRequest activityRequest) {
        LocalDate recordDate = activityRequest.recordDate();
        this.validateDuplicateActivity(memberId, recordDate);

        Member member = memberService.findById(memberId);
        Activity activity = Activity.builder(member)
                .waterIntake(activityRequest.waterIntake())
                .smokedCigarettes(activityRequest.smokedCigarettes())
                .alcoholIntake(activityRequest.alcoholIntake())
                .recordDate(recordDate)
                .build();

        activityRepository.save(activity);
        return activity.getId();
    }

    private void validateDuplicateActivity(Long memberId, LocalDate recordDate) {
        Optional<Activity> findActivity = activityRepository.findByMemberIdAndRecordDate(memberId, recordDate);
        if (findActivity.isPresent()) {
            throw new DuplicateEntityException(DUPLICATED_ACTIVITY);
        }
    }
}
