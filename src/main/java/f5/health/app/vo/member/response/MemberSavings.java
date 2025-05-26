package f5.health.app.vo.member.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 절약 금액")
public interface MemberSavings {

    @Schema(description = "음주 절약 금액", example = "50000")
    int getAlcoholSavedMoney();

    @Schema(description = "흡연 절약 금액", example = "0")
    int getSmokingSavedMoney();

    @Schema(description = "절약 금액에 대한 GPT 건강 물품 추천 코멘트", example = "\uD83D\uDCB0 총 절약 금액이 5,000원을 넘으면, AI가 나에게 딱 맞는 건강 물품을 추천해 드려요.")
    String getHealthItemsRecommend();
}
