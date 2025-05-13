package f5.health.app.constant.member.badge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "배지(Enum) 응답")
public class BadgeModel {

    @Schema(description = "해당 배지 점수 하한선", example = "25000")
    private final long cutOffScore;

    public BadgeModel(Badge badge) {
        this.cutOffScore = badge.getCutOffScore();
    }
}
