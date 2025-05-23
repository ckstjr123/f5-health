package f5.health.app.vo.healthreport.request;

import f5.health.app.service.healthreport.vo.MealsNutritionContents;
import f5.health.app.service.healthreport.vo.request.healthkit.HealthKit;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
/** GptController 용*/
public class HealthFeedbackRequest {
    private HealthKit healthKit;
    private MealsNutritionContents nutritionContents;
    private String gender;
    private int height;
    private int weight;
    private int recommendedCalories;
}
