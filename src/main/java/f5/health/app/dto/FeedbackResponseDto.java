package f5.health.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedbackResponseDto {
    private int lifestyleScore;
    private String feedbackMessage;
}