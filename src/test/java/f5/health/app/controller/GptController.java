package f5.health.app.controller;

import f5.health.app.service.GptService;
import f5.health.app.service.healthreport.dto.FeedbackRequestDto;
import f5.health.app.service.healthreport.dto.FeedbackResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptFeedbackService;


    @PostMapping("/generate")
    public FeedbackResponseDto getScoreAndFeedback(@RequestBody FeedbackRequestDto dto) {
        return gptFeedbackService.getScoreAndFeedback(dto);
    }
}
