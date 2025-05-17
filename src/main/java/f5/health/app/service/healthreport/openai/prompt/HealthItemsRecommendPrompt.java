package f5.health.app.service.healthreport.openai.prompt;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import f5.health.app.entity.Member;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.Workouts;

public class HealthItemsRecommendPrompt implements Prompt {

    private static final int MAX_TOKENS = 40;
    private final int totalSavedMoney;
    private String nickname;
    private final int height, weight;
    private final String gender;
    private int daySmokeCigarettes;
    private int weekAlcoholDrinks;
    private final Workouts workouts;

    public HealthItemsRecommendPrompt(Member member, Workouts workouts) {
        this.totalSavedMoney = member.getTotalSavedMoney();
        this.nickname = member.getNickname();
        this.height = member.getHeight();
        this.gender = member.getGender().label();
        this.weight = member.getWeight();
        this.daySmokeCigarettes = member.getDaySmokeCigarettes();
        this.weekAlcoholDrinks = member.getWeekAlcoholDrinks();
        this.workouts = workouts;
    }

    @Override
    public ChatCompletionRequest generate() {
        return null;
    }
}
