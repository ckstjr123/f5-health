package f5.health.app.dto;

import lombok.Data;

@Data
public class FeedbackRequestDto {

    // 1. 요청 타입 (daily / weekly / monthly)
    private String type;

    // 2. 상세 건강 수치 (daily 용)
    private Integer waterIntake;  //음수량
    private Integer alcoholAmount;  //음주량
    private Integer smokingAmount;  //흡연량
    private Integer stepCount;  //걸음수
    private String exerciseType; //운동 종목
    private Integer exerciseDuration; //운동 시간
    private Integer exerciseCalories; //운동 칼로리
    private Integer heartRate; // 평상시 심박수
    private Integer totalCaloriesBurned; // 활동+운동 칼로리
    private Integer sleepHours;// 취침시간

    // 3. 식단 (칼로리, 탄단지)
    private Double kcal; //섭취 칼로리
    private Double carbohydrate; //탄수화물
    private Double protein; //단백질
    private Double fat; //지방

    // 4. 요약된 텍스트 직접 전달 (weekly/monthly 용)
    private String input;
}
