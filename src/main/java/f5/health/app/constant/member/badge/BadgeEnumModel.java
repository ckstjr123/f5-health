package f5.health.app.constant.member.badge;

import f5.health.app.constant.EnumModel;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "배지(Enum) 응답")
public final class BadgeEnumModel extends EnumModel {

    @Schema(description = "해당 배지 점수 하한선", example = "25000")
    private final long cutOffScore;

    public BadgeEnumModel(Badge badge) {
        super(badge);
        this.cutOffScore = badge.getCutOffScore();
    }

    public long getCutOffScore() {
        return this.cutOffScore;
    }
}
