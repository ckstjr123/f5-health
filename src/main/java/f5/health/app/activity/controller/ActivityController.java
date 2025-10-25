package f5.health.app.activity.controller;

import f5.health.app.activity.controller.vo.CreateActivityResponse;
import f5.health.app.activity.domain.AlcoholType;
import f5.health.app.activity.service.ActivityService;
import f5.health.app.activity.vo.ActivityRequest;
import f5.health.app.activity.vo.ActivityResponse;
import f5.health.app.activity.vo.ActivityUpdateRequest;
import f5.health.app.auth.Login;
import f5.health.app.auth.vo.LoginMember;
import f5.health.app.common.EnumModel;
import f5.health.app.common.EnumModelMapper;
import f5.health.app.common.vo.Date;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class ActivityController implements ActivityApiDocs {

    private final EnumModelMapper enumMapper;
    private final ActivityService activityService;

    @GetMapping("/alcohol-types")
    public List<? extends EnumModel> alcoholTypes() {
        return enumMapper.get(AlcoholType.class);
    }

    @GetMapping
    public ActivityResponse find(@Login LoginMember loginMember, @ModelAttribute("date") @Valid Date date) {
        return activityService.findOne(loginMember.getId(), date.get());
    }


    @PostMapping
    public ResponseEntity<CreateActivityResponse> save(@Login LoginMember loginMember, @RequestBody @Valid ActivityRequest activityRequest) {
        Long activityId = activityService.save(loginMember.getId(), activityRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreateActivityResponse(activityId));
    }

    @PatchMapping("/{activityId}/edit")
    public void updateActivity(@PathVariable("activityId") Long activityId, @Login LoginMember loginMember,
                               @RequestBody @Valid ActivityUpdateRequest activityUpdateRequest) {
        activityService.updateActivity(activityId, activityUpdateRequest, loginMember.getId());
    }

    @PutMapping("/{activityId}/{alcoholType}")
    public void upsertAlcoholConsumption(@PathVariable("activityId") Long activityId, @PathVariable("alcoholType") AlcoholType alcoholType,
                                         @Login LoginMember loginMember, @RequestBody @Valid ActivityRequest.AlcoholConsumptionParam alcoholParam) {
        activityService.upsertAlcoholConsumption(activityId, alcoholParam, loginMember.getId());
    }

    @DeleteMapping("/{activityId}/{alcoholType}")
    public ResponseEntity<Void> deleteAlcoholConsumption(@PathVariable("activityId") Long activityId, @PathVariable("alcoholType") AlcoholType alcoholType,
                                                         @Login LoginMember loginMember) {
        activityService.deleteAlcoholConsumption(activityId, alcoholType, loginMember.getId());
        return ResponseEntity.noContent().build();
    }

}
