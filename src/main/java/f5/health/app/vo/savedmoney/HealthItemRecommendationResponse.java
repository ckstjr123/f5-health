package f5.health.app.vo.savedmoney;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "절약 금액 및 추천 건강 아이템 응답")
public class HealthItemRecommendationResponse {

    @Schema(description = "현재까지 절약한 금액 (원)", example = "15800")
    private final int savedMoney;

    @Schema(description = "추천 건강 아이템 문장", example = "15800원으로 비타민B, 요가매트 등을 구매할 수 있어요!")
    private final String recommendation;

    // 정적 팩토리 메서드로도 제공 가능
    public static HealthItemRecommendationResponse of(int savedMoney, String recommendation) {
        return new HealthItemRecommendationResponse(savedMoney, recommendation);
    }
}
