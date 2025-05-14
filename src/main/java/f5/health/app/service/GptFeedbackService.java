package f5.health.app.service;

import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;
import f5.health.app.dto.FeedbackRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GptFeedbackService {

    private final OpenAiService openAiService;

    public String getFeedback(FeedbackRequestDto dto) {
        String prompt;

        if (dto.getInput() != null && !dto.getInput().isBlank()) {
            // 요약 텍스트 기반 요청
            prompt = String.format("""
                건강 리포트 요약: %s
                이 내용을 바탕으로 한국어로 건강 피드백을 제공해 주세요.
            """, dto.getInput());
        } else {
            // 일일 수치 기반 요청
            prompt = String.format("""
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
                - 섭취 칼로리: %d kcal
                - 탄수화물: %.1f g
                - 단백질: %.1f g
                - 지방: %.1f g

                위 데이터를 바탕으로 한국어 건강 피드백을 작성해 주세요. 
            """,
                    dto.getWaterIntake(), dto.getSmokingAmount(), dto.getAlcoholAmount(), dto.getStepCount(),
                    dto.getExerciseType(), dto.getExerciseDuration(), dto.getExerciseCalories(), dto.getHeartRate(),
                    dto.getTotalCaloriesBurned(), dto.getSleepHours(),
                    dto.getKcal(), dto.getCarbohydrate(), dto.getProtein(), dto.getFat());
        }

        ChatMessage systemMessage = new ChatMessage("system", "당신은 건강 코치입니다.");
        ChatMessage userMessage = new ChatMessage("user", prompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-4o")
                .messages(List.of(systemMessage, userMessage))
                .temperature(0.7)
                .maxTokens(100)
                .build();

        ChatCompletionResult result = openAiService.createChatCompletion(request);
        return result.getChoices().get(0).getMessage().getContent();
    }
}