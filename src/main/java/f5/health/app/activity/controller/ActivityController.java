package f5.health.app.activity.controller;

import f5.health.app.activity.constant.AlcoholType;
import f5.health.app.activity.service.ActivityService;
import f5.health.app.activity.service.ActivityRequest;
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

    @GetMapping("/alcohol-types")
    public List<? extends EnumModel> alcoholTypes() {
        return enumMapper.get(AlcoholType.class);
    }

    @GetMapping
    public ActivityResponse findActivity(@Login LoginMember loginMember,
                                         @ModelAttribute("date") @Valid RecordDate date) {
        return activityService.findActivity(loginMember.getId(), date.get());
    }

    @PostMapping
    public ResponseEntity<Void> save(@Login LoginMember loginMember,
                                     @RequestBody @Valid ActivityRequest activityRequest) {
        activityService.save(loginMember.getId(), activityRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
