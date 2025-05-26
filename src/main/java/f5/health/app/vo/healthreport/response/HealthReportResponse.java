package f5.health.app.vo.healthreport.response;

import f5.health.app.entity.healthreport.HealthReport;
import f5.health.app.entity.meal.Meal;
import f5.health.app.vo.meal.response.MealResponse;
import f5.health.app.vo.meal.response.MealsResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

@Getter
@Schema(description = "건강 일지 저장/조회 응답")
public class HealthReportResponse {

    @Schema(description = "받은 점수", example = "80")
    private final int score;

    @Schema(description = "수분 섭취량(ml)", example = "650")
    private final int waterIntake;

    @Schema(description = "피운 담배", example = "6")
    private final int smokeCigarettes;

    @Schema(description = "마신 술잔", example = "0")
    private final int alcoholDrinks;

    @Schema(description = "GPT 건강 피드백 결과")
    private final String healthFeedback;

    @Schema(description = "기록된 식단")
    private final MealsResponse mealsResponse;

    @Schema(description = "건강 데이터 수집 기점", example = "2025-05-17T22:30")
    private final LocalDateTime startDateTime;

    @Schema(description = "리포트 기록 알림 시각(yyyy-MM-dd HH:mm)", example = "2025-05-18T22:30")
    private final LocalDateTime endDateTime;

    @Schema(description = "리포트 작성 날짜", example = "2025-05-19T00:10")
    private final LocalDateTime reportedAt;

    public HealthReportResponse(HealthReport report, int recommendedCalories, List<Meal> meals) {
        this.score = report.getHealthLifeScore();
        this.waterIntake = report.getWaterIntake();
        this.smokeCigarettes = report.getSmokeCigarettes();
        this.alcoholDrinks = report.getAlcoholDrinks();
        this.healthFeedback = report.getHealthFeedback();
        this.mealsResponse = MealsResponse.of(
                recommendedCalories,
                meals.stream()
                        .map(meal -> MealResponse.only(meal))
                        .toList());
        this.startDateTime = report.getStartDateTime();
        this.endDateTime = LocalDateTime.of(report.getEndDate(), report.getEndTime());
        this.reportedAt = report.getReportedAt().truncatedTo(SECONDS);
    }
}
