package f5.health.app.controller;

import f5.health.app.dto.FeedbackRequestDto;
import f5.health.app.service.GptFeedbackService;
import f5.health.app.dto.FeedbackResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class GptFeedbackController {

    private final GptFeedbackService gptFeedbackService;


    @PostMapping("/generate")
    public FeedbackResponseDto getScoreAndFeedback(@RequestBody FeedbackRequestDto dto) {
        return gptFeedbackService.getScoreAndFeedback(dto);
    }
}
