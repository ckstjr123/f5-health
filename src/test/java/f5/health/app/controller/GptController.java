package f5.health.app.controller;

import f5.health.app.entity.Member;
import f5.health.app.service.healthreport.openai.GptService;
import f5.health.app.service.healthreport.openai.prompt.HealthFeedbackPrompt;
import f5.health.app.vo.healthreport.request.HealthFeedbackRequest;
import f5.health.app.entity.healthreport.PromptCompletion;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/health")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptFeedbackService;

    @PostMapping("/feedback")
    public PromptCompletion healthFeedback(@RequestBody Member member, @RequestBody HealthFeedbackRequest request) {
        return gptFeedbackService.call(new HealthFeedbackPrompt(member, request.getHealthKit(), request.getNutritionFacts()));
    }
}
