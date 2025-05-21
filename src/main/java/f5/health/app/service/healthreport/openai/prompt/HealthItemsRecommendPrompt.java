package f5.health.app.service.healthreport.openai.prompt;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import f5.health.app.entity.Member;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.Workouts;

import java.util.List;

import static f5.health.app.config.OpenAIConfig.*;

public class HealthItemsRecommendPrompt implements Prompt {

    private static final int MAX_TOKENS = 50;
    private final int totalSavedMoney;
    private final String nickname;
    private final String gender;
    private final int height, weight;
    private final int daySmokeCigarettes;
    private final int weekAlcoholDrinks;
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
        String prompt = String.format("""
                다음은 사용자의 건강 및 생활 정보입니다:
                
                - 닉네임: %s
                - 성별: %s
                - 키: %d cm
                - 몸무게: %d kg
                - 절약한 금액: %,d 원
                - 하루 평균 흡연량: %d 개비
                - 주간 음주량: %d 잔
                - 최근 운동 기록: %s
                
                위의 정보를 바탕으로 다음 내용을 한국어로 간결하게 작성해 주세요:
                
                1. 현재 절약한 금액으로 구매 가능한 건강 관련 물품 또는 식단 예시 1~2개
                2. 약간만 더 모으면 구매 가능한 건강 관련 추천 아이템 예시 1~2개
                (각 항목은 금액과 함께 제시해 주세요)
                
                추천은 실질적이고 현실적인 제품(예: 영양제, 운동기구, 건강식 등)을 중심으로 구성해 주세요.
                문장은 짧고 명확하게 구성해 주세요.
                """
                , nickname, gender, height, weight, totalSavedMoney, daySmokeCigarettes, weekAlcoholDrinks
                , workouts.getWorkoutTypes().isEmpty() ? "기록 없음" : String.join(", ", workouts.getWorkoutTypes()));

        return ChatCompletionRequest.builder()
                .model(OPEN_AI_MODEL)
                .messages(List.of(new com.theokanning.openai.completion.chat.ChatMessage(ChatMessageRole.SYSTEM.value(), "당신은 건강 코치이며, 절약 금액을 바탕으로 건강에 도움이 되는 물품이나 식단을 추천하는 역할입니다. 예산에 맞는 실질적 추천을 제안하세요.")
                        , new com.theokanning.openai.completion.chat.ChatMessage(ChatMessageRole.USER.value(), prompt)))
                .temperature(SAMPLING_TEMPERATURE)
                .maxTokens(MAX_TOKENS)
                .stop(STOP_DELIMITERS)
                .build();
    }
}
