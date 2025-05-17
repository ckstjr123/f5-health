package f5.health.app.service.healthreport.openai.prompt;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import f5.health.app.service.healthreport.vo.request.NutritionFacts;
import f5.health.app.service.healthreport.vo.request.healthkit.CustomHealthKit;
import f5.health.app.service.healthreport.vo.request.healthkit.HealthKit;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.Activity;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.SleepAnalysis;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.VitalSigns;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.Workouts;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import static f5.health.app.config.OpenAIConfig.OPEN_AI_MODEL;
import static f5.health.app.config.OpenAIConfig.SAMPLING_TEMPERATURE;

public class HealthFeedbackPrompt implements Prompt {

    private static final int MAX_TOKENS = 100;
    private final CustomHealthKit customKit;
    private final Activity activity;
    private final SleepAnalysis sleepAnalysis;
    private final Workouts workouts;
    private final VitalSigns vitalSigns;
    private final NutritionFacts nutritionFacts;

    public HealthFeedbackPrompt(HealthKit healthKit, NutritionFacts nutritionFacts) {
        this.customKit = healthKit.getCustomHealthKit();
        this.activity = healthKit.getActivity();
        this.sleepAnalysis = healthKit.getSleepAnalysis();
        this.workouts = healthKit.getWorkouts();
        this.vitalSigns = healthKit.getVitalSigns();
        this.nutritionFacts = nutritionFacts;
    }

    @Override
    public ChatCompletionRequest generate() {
        Set<String> workoutTypes = workouts.getWorkoutTypes();

        String prompt = MessageFormat.format("""
                        다음은 사용자의 일일 건강 기록입니다:
                        
                        음수량: %d ml
                        흡연량: %d 개비
                        음주량: %d 잔
                        걸음 수: %d보"""
                        + (!workoutTypes.isEmpty() ? String.format("운동 종류: %s", workoutTypes.toArray()) : "") + """
                        운동 시간: %.0f분
                        소모 칼로리: %d kcal
                        심박수: %d bpm
                        렘 수면: %d시간
                        깊은 수면: %d시간
                        섭취 칼로리: %d kcal
                        탄수화물: %.1f g
                        단백질: %.1f g
                        지방: %.1f g
                        
                        위 데이터를 기반으로 아래 항목을 포함하는 건강 피드백을 작성해 주세요:
                        1. 사용자의 현재 생활 습관에 대한 요약 평가
                        2. 개선이 필요한 점이 있다면 구체적이고 실천 가능한 조언
                        3. 잘하고 있는 부분은 칭찬과 유지 권장
                        
                        * 문장은 간결하고 명확하게 구성해 주세요.
                        * 진지하지만 너무 무겁지 않게 조언해 주세요.
                        """
                , customKit.getWaterIntake(), customKit.getSmokedCigarettes(), customKit.getConsumedAlcoholDrinks()
                , activity.getStepCount(), activity.getAppleExerciseTime(), activity.getActiveEnergyBurned()
                , vitalSigns.getHeartRate()
                , sleepAnalysis.getAsleepREM(), sleepAnalysis.getAsleepDeep()
                , nutritionFacts.getTotalKcal(), nutritionFacts.getTotalCarbohydrate(), nutritionFacts.getTotalProtein(), nutritionFacts.getTotalFat()
        );

        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), """
                    당신은 건강 데이터 분석을 기반으로 건강 습관 개선을 도와주는 전문적인 건강 코치입니다.
                    당신의 목표는 사용자의 일일 건강 데이터를 분석하여 간결하지만 유익하고 실질적인 건강 피드백을 제공하는 것입니다.
                    피드백은 **한국어**로 작성하고, 문장은 자연스럽고 따뜻한 어조로 구성해주세요.
                    숫자 데이터는 해석해주되, 너무 단순 나열하지 말고 사용자의 개선 방향을 제안하는 방식으로 말해주세요.
                """);
        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt);

        return ChatCompletionRequest.builder()
                .model(OPEN_AI_MODEL)
                .messages(List.of(systemMessage, userMessage))
                .temperature(SAMPLING_TEMPERATURE)
                .maxTokens(MAX_TOKENS)
                .build();
    }
}
