package f5.health.app.service;

import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;
import f5.health.app.dto.FeedbackRequestDto;
import f5.health.app.dto.FeedbackResponseDto;
import f5.health.app.score.LifestyleScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GptFeedbackService {

    private final OpenAiService openAiService;
    private final LifestyleScoreCalculator scoreCalculator = new LifestyleScoreCalculator();

    public FeedbackResponseDto getScoreAndFeedback(FeedbackRequestDto dto) {

        // 1. 점수 계산
        LifestyleScoreCalculator calculator = new LifestyleScoreCalculator();
        int score = 0;
        score += calculator.waterScore(dto.getWaterIntake());
        score += calculator.smokingScore(dto.getSmokingAmount());
        score += calculator.alcoholScore(dto.getAlcoholAmount());
        score += calculator.stepScore(dto.getStepCount());
        score += calculator.workoutScore(dto.getExerciseDuration(), dto.getExerciseCalories());
        score += calculator.heartRateScore(dto.getHeartRate());
        score += calculator.totalCaloriesBurnedScore(dto.getTotalCaloriesBurned());
        score += calculator.sleepScore(dto.getSleepHours());
        score += calculator.intakeCaloriesScore(dto.getKcal());
        score += calculator.pfcBalanceScore(dto.getCarbohydrate(), dto.getProtein(), dto.getFat());

        // 2. GPT 피드백 생성
        String prompt = String.format("""
            사용자의 일일 건강 데이터:

            - 물 섭취량: %d ml
            - 흡연량: %d 개비
            - 음주량: %d 잔
            - 걸음 수: %d
            - 운동 종류: %s
            - 운동 시간: %d 초
            - 운동 칼로리 소모: %d kcal
            - 심박수: %d bpm
            - 총 소모 칼로리: %d kcal
            - 수면 시간: %d 시간
            - 섭취 칼로리: %.1f kcal
            - 탄수화물: %.1f g
            - 단백질: %.1f g
            - 지방: %.1f g

            위 데이터를 바탕으로 한국어 건강 피드백을 작성해 주세요.
        """,
                dto.getWaterIntake(), dto.getSmokingAmount(), dto.getAlcoholAmount(), dto.getStepCount(),
                dto.getExerciseType(), dto.getExerciseDuration(), dto.getExerciseCalories(), dto.getHeartRate(),
                dto.getTotalCaloriesBurned(), dto.getSleepHours(), /*
        );

        ChatMessage systemMessage = new ChatMessage("system", "당신은 건강 코치입니다.");
        ChatMessage userMessage = new ChatMessage("user", prompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-4o")
                .messages(List.of(systemMessage, userMessage))
                .temperature(0.7)
                .maxTokens(100)
                .build();

        ChatCompletionResult result = openAiService.createChatCompletion(request);
        String feedback = result.getChoices().get(0).getMessage().getContent();

        return new FeedbackResponseDto(score, feedback);
    }
}