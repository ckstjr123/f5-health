package f5.health.app.controller;

import f5.health.app.service.openai.GptService;
import f5.health.app.service.openai.HealthFeedbackPrompt;
import f5.health.app.service.healthreport.vo.request.NutritionFacts;
import f5.health.app.vo.openai.response.PromptCompletion;
import f5.health.app.service.healthreport.vo.request.healthkit.HealthKit;
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
    public PromptCompletion healthFeedback(@RequestBody HealthKit healthKit, @RequestBody NutritionFacts nutritionFacts) {
        return gptFeedbackService.call(new HealthFeedbackPrompt(healthKit, nutritionFacts));
    }
}
