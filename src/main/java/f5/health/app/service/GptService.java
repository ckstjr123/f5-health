package f5.health.app.service;

import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;
import f5.health.app.service.healthreport.dto.FeedbackRequestDto;
import f5.health.app.service.healthreport.dto.FeedbackResponseDto;
import f5.health.app.service.healthreport.HealthLifeScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GptService {

    private final OpenAiService openAiService;
    private final HealthLifeScoreCalculator scoreCalculator = new HealthLifeScoreCalculator();

    public FeedbackResponseDto getScoreAndFeedback(FeedbackRequestDto dto) {

        // 1. 점수 계산
        HealthLifeScoreCalculator calculator = new HealthLifeScoreCalculator();
        int score = 0;
        score += calculator.waterScore(dto.getWaterIntake());
        score += calculator.smokingScore(dto.getSmokingAmount());
        score += calculator.alcoholScore(dto.getAlcoholAmount());
        score += calculator.stepScore(dto.getStepCount());
        score += calculator.workoutScore(dto.getExerciseDuration(), dto.getExerciseCalories());
        score += calculator.heartRateScore(dto.getHeartRate());
        score += calculator.totalCaloriesBurnedScore(dto.getTotalCaloriesBurned());
        score += calculator.sleepScore(dto.getSleepHours());
//        score += calculator.intakeCaloriesScore(dto.getKcal());
//        score += calculator.pfcBalanceScore(dto.getCarbohydrate(), dto.getProtein(), dto.getFat());

        // 2. GPT 피드백 생성
        String prompt = String.format("""
            다음은 한 사용자의 일일 건강 기록입니다:

            물 섭취량: %d ml
            흡연량: %d 개비
            음주량: %d 잔
            걸음 수: %d 걸음
            운동 종류: %s
            운동 시간: %d 초
            운동 칼로리 소모: %d kcal
            심박수: %d bpm
            총 소모 칼로리: %d kcal
            수면 시간: %d 시간
            섭취 칼로리: %.1f kcal
            탄수화물: %.1f g
            단백질: %.1f g
            지방: %.1f g

            이 데이터를 기반으로 아래 항목을 포함하는 한국어 피드백을 작성해 주세요:
            1. 사용자의 현재 생활 습관에 대한 요약 평가
            2. 개선이 필요한 부분이 있다면 구체적인 조언
            3. 잘하고 있는 부분은 칭찬과 유지 권장

            * 문장은 간결하고 명확하게 구성해 주세요.
            * 너무 딱딱하지 않게, 조언해주는 느낌으로 말해주세요.
            """,
                dto.getWaterIntake(), dto.getSmokingAmount(), dto.getAlcoholAmount(), dto.getStepCount(),
                dto.getExerciseType(), dto.getExerciseDuration(), dto.getExerciseCalories(), dto.getHeartRate(),
                dto.getTotalCaloriesBurned(), dto.getSleepHours() /*나중에 추가 예정*/
        );

        ChatMessage systemMessage = new ChatMessage("system", """
            당신은 건강 데이터 분석을 기반으로 건강 습관 개선을 도와주는 전문적인 건강 코치입니다.
            당신의 목표는 사용자의 일일 건강 데이터를 분석하여 간결하지만 유익하고 실질적인 건강 피드백을 제공하는 것입니다.
            피드백은 **한국어**로 작성하고, 문장은 자연스럽고 친근한 말투로 구성해주세요.
            숫자 데이터는 해석해주되, 너무 단순 나열하지 말고 사용자의 개선 방향을 제안하는 방식으로 말해주세요.
        """);
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