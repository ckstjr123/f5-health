package f5.health.app.vo.savedmoney;

import f5.health.app.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "절약 금액 및 추천 건강 아이템 응답")
public class HealthItemRecommendationResponse {

    @Schema(description = "흡연 절약 금액", example = "8000")
    private final int smokingSavedMoney;

    @Schema(description = "음주 절약 금액", example = "7800")
    private final int alcoholSavedMoney;

    @Schema(description = "추천 건강 아이템 문장", example = "15800원으로 비타민B, 요가매트 등을 구매할 수 있어요!")
    private final String recommendation;

    public static HealthItemRecommendationResponse of(Member member) {
        return new HealthItemRecommendationResponse(
                member.getSmokingSavedMoney(),
                member.getAlcoholSavedMoney(),
                member.getHealthItemsRecommend()
        );
    }
}
