package f5.health.app.service.healthreport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedbackResponseDto {
    private int healthLifeScore;
    private String feedbackMessage;
}