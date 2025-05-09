package f5.health.app.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FeedbackRequestDto {

    // 1. 요청 타입 (daily / weekly / monthly)
    private String type;

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

    // 3. 식단 (칼로리, 탄단지)
    private Integer kcal;
    private Double carbohydrate;
    private Double protein;
    private Double fat;

    // 4. 요약된 텍스트 직접 전달 (weekly/monthly 용)
    private String input;
}
