package f5.health.app.activity.service;

import f5.health.app.activity.domain.Activity;
import f5.health.app.activity.repository.ActivityRepository;
import f5.health.app.activity.vo.ActivityRequest;
import f5.health.app.activity.vo.ActivityResponse;
import f5.health.app.activity.vo.ActivityUpdateRequest;
import f5.health.app.auth.vo.LoginMember;
import f5.health.app.common.exception.AccessDeniedException;
import f5.health.app.common.exception.ConflictException;
import f5.health.app.common.exception.NotFoundException;
import f5.health.app.member.entity.Member;
import f5.health.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static f5.health.app.activity.exception.ActivityErrorCode.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ActivityService {

    private final MemberService memberService;
    private final ActivityRepository activityRepository;


    public ActivityResponse findOne(Long memberId, LocalDate recordedDate) {
        Activity activity = activityRepository.findActivityJoinFetch(memberId, recordedDate)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACTIVITY));
        return ActivityResponse.from(activity);
    }

    public Long save(Long memberId, ActivityRequest request) {
        Member member = memberService.findById(memberId);
        validateDuplicateActivity(memberId, request.getRecordedDate());

        Activity activity = Activity.createActivity(member, request.getAlcoholParams())
                .waterIntake(request.getWaterIntake())
                .smokedCigarettes(request.getSmokedCigarettes())
                .recordedDate(request.getRecordedDate())
                .build();

        activityRepository.save(activity);
        return activity.getId();
    }

    public void updateActivity(Long activityId, ActivityUpdateRequest updateRequest, LoginMember loginMember) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACTIVITY));
        validateActivityOwnership(activity, loginMember.getId());

        activity.update(updateRequest.getWaterIntake(), updateRequest.getSmokedCigarettes());
    }

    public void saveOrUpdateAlcoholConsumption(Long activityId, ActivityRequest.AlcoholConsumptionParam alcoholParam, LoginMember loginMember) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACTIVITY));
        validateActivityOwnership(activity, loginMember.getId());

        activity.addOrUpdateAlcoholConsumption(alcoholParam.alcoholType(), alcoholParam.intake());
    }


    private void validateDuplicateActivity(Long memberId, LocalDate recordedDate) {
        Optional<Activity> findActivity = activityRepository.findByMemberIdAndRecordedDate(memberId, recordedDate);
        if (findActivity.isPresent()) {
            throw new ConflictException(DUPLICATED_ACTIVITY);
        }
    }

    private void validateActivityOwnership(Activity activity, Long memberId) {
        if (!activity.isOwnedBy(memberId)) {
            throw new AccessDeniedException(NOT_FOUND_ACTIVITY_OWNERSHIP);
        }
    }
}
