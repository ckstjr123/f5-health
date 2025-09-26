package f5.health.app.activity.controller;

import f5.health.app.auth.jwt.vo.JwtMember;
import f5.health.app.activity.service.ActivityService;
import f5.health.app.activity.service.request.ActivityRequest;
import f5.health.app.activity.vo.ActivityResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class ActivityController implements ActivityApiDocs {

    private final ActivityService activityService;

    @GetMapping
    public ActivityResponse findActivity(@AuthenticationPrincipal JwtMember loginMember,
                                         @ModelAttribute("date") @Valid RecordDate recordDate) {
        return activityService.findActivity(loginMember.id(), recordDate.get());
    }

    @PostMapping
    public ResponseEntity<Void> save(@AuthenticationPrincipal JwtMember loginMember,
                                     @RequestBody @Valid ActivityRequest activityRequest) {
        activityService.save(loginMember.id(), activityRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
