package f5.health.app.activity.controller;

import f5.health.app.activity.constant.AlcoholType;
import f5.health.app.activity.controller.vo.CreateActivityResponse;
import f5.health.app.activity.controller.vo.RecordDate;
import f5.health.app.activity.repository.AlcoholConsumptionRepository;
import f5.health.app.activity.service.ActivityService;
import f5.health.app.activity.vo.ActivityRequest;
import f5.health.app.activity.vo.ActivityResponse;
import f5.health.app.auth.Login;
import f5.health.app.auth.vo.LoginMember;
import f5.health.app.common.EnumModel;
import f5.health.app.common.EnumModelMapper;
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
    private final AlcoholConsumptionRepository alcoholConsumptionRepository;

    @GetMapping("/alcohol-types")
    public List<? extends EnumModel> alcoholTypes() {
        return enumMapper.get(AlcoholType.class);
    }

    @GetMapping
    public ActivityResponse findOne(@Login LoginMember loginMember,
                                    @ModelAttribute("date") @Valid RecordDate date) {
        return activityService.findOne(loginMember.getId(), date.get());
    }


    @PostMapping
    public ResponseEntity<CreateActivityResponse> save(@Login LoginMember loginMember, @RequestBody @Valid ActivityRequest activityRequest) {
        Long activityId = activityService.save(loginMember.getId(), activityRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreateActivityResponse(activityId));
    }

    @PutMapping("/{activityId}/{alcoholType}")
    public void saveOrUpdateAlcoholConsumption(@PathVariable("activityId") Long activityId, @PathVariable("alcoholType") AlcoholType alcoholType,
                                               @Login LoginMember loginMember, @RequestBody @Valid ActivityRequest.AlcoholConsumptionParam alcoholParam) {
        activityService.saveOrUpdateAlcoholConsumption(activityId, alcoholParam, loginMember);
    }

}
