package f5.health.app.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FeedbackRequestDto {

    // 2. 상세 건강 수치 (daily 용)
    private Integer waterIntake;
    private Integer alcoholAmount;
    private Integer smokingAmount;
    private Integer stepCount;
    private String exerciseType;
    private Integer exerciseDuration;
    private Integer exerciseCalories;
    private Integer heartRate;
    private Integer totalCaloriesBurned;
    private Integer sleepHours;

}
