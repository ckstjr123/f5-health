package f5.health.app.service.healthreport.openai.prompt;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.Workouts;

public class HealthItemsRecommendPrompt implements Prompt {

    private static final int MAX_TOKENS = 40;
    private final int totalSavedMoney;
    private final Workouts workouts;

    public HealthItemsRecommendPrompt(int totalSavedMoney, int height, int weight, Workouts workouts) {
        this.totalSavedMoney = totalSavedMoney;
        this.workouts = workouts;
    }

    @Override
    public ChatCompletionRequest generate() {
        return null;
    }
}
