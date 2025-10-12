package f5.health.app.activity.service;

import f5.health.app.activity.domain.Activity;
import f5.health.app.activity.domain.alcoholconsumption.AlcoholConsumption;
import f5.health.app.activity.domain.alcoholconsumption.AlcoholConsumptionFactory;
import f5.health.app.activity.domain.alcoholconsumption.AlcoholConsumptionId;
import f5.health.app.activity.repository.ActivityRepository;
import f5.health.app.activity.repository.AlcoholConsumptionRepository;
import f5.health.app.activity.vo.ActivityRequest;
import f5.health.app.activity.vo.ActivityResponse;
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
import java.util.List;
import java.util.Optional;

import static f5.health.app.activity.constant.ActivityErrorCode.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ActivityService {

    private final MemberService memberService;
    private final ActivityRepository activityRepository;
    private final AlcoholConsumptionRepository alcoholConsumptionRepository;

    public ActivityResponse findOne(Long memberId, LocalDate recordDate) {
        // todo: 조인해서 조회
        Activity activity = activityRepository.findByMemberIdAndRecordDate(memberId, recordDate)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACTIVITY));
        return ActivityResponse.from(activity);
    }

    public Long save(Long memberId, ActivityRequest activityRequest) {
        Member member = memberService.findById(memberId);
        validateDuplicateActivity(memberId, activityRequest.getRecordDate());

        List<AlcoholConsumption> alcoholConsumptions = AlcoholConsumptionFactory.from(activityRequest.getAlcoholParams());
        Activity activity = Activity.createActivity(member, alcoholConsumptions)
                .waterIntake(activityRequest.getWaterIntake())
                .smokedCigarettes(activityRequest.getSmokedCigarettes())
                .recordDate(activityRequest.getRecordDate())
                .build();

        activityRepository.save(activity);
        return activity.getId();
    }

    public void addAlcoholConsumption(Long activityId, ActivityRequest.AlcoholConsumptionParam alcoholParam, LoginMember loginMember) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACTIVITY));
        validateActivityOwnership(activity, loginMember.getId());

        AlcoholConsumptionId alcoholConsumptionId = AlcoholConsumptionId.of(activityId, alcoholParam.alcoholType());
        Optional<AlcoholConsumption> alcoholConsumption = alcoholConsumptionRepository.findById(alcoholConsumptionId);

        alcoholConsumption.ifPresentOrElse(
                ac -> ac.update(alcoholParam.intake()),
                () -> saveAlcoholConsumption(activity, alcoholParam)
        );
    }


    private void saveAlcoholConsumption(Activity activity, ActivityRequest.AlcoholConsumptionParam alcoholParam) {
        AlcoholConsumption alcoholConsumption = AlcoholConsumptionFactory.of(alcoholParam.alcoholType(), alcoholParam.intake());
        activity.addAlcoholConsumption(alcoholConsumption);
    }

    private void validateDuplicateActivity(Long memberId, LocalDate recordDate) {
        Optional<Activity> findActivity = activityRepository.findByMemberIdAndRecordDate(memberId, recordDate);
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
