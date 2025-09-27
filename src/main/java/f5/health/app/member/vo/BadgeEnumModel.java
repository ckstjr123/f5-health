package f5.health.app.member.vo;

import f5.health.app.common.EnumModel;
import f5.health.app.member.constant.Badge;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "배지(Enum) 응답")
public final class BadgeEnumModel extends EnumModel {

    @Schema(description = "해당 배지 포인트 하한선", example = "25000")
    private final long cutOffPoint;

    public BadgeEnumModel(Badge badge) {
        super(badge);
        this.cutOffPoint = badge.cutOffPoint();
    }

    public long getCutOffPoint() {
        return this.cutOffPoint;
    }

    @Override
    public String toString() {
        return "BadgeEnumModel{" +
                "value='" + super.getValue() + '\'' +
                ", label='" + super.getLabel() + '\'' +
                ", cutOffPoint=" + cutOffPoint +
                '}';
    }
}
