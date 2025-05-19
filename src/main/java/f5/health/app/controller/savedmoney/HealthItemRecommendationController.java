package f5.health.app.controller.savedmoney;

import f5.health.app.jwt.JwtMember;
import f5.health.app.service.savedmoney.HealthItemRecommendService;
import f5.health.app.vo.savedmoney.HealthItemRecommendationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/health/items")
@RequiredArgsConstructor
public class HealthItemRecommendationController {

    private final HealthItemRecommendService recommendService;

    @GetMapping("/recommend")
    public HealthItemRecommendationResponse recommendItems(@AuthenticationPrincipal JwtMember loginMember) {
        return recommendService.recommend(loginMember.getId());
    }
}
