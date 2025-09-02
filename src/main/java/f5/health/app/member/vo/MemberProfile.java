package f5.health.app.member.vo;

import f5.health.app.common.EnumModel;
import f5.health.app.common.EnumModelMapper;
import f5.health.app.member.constant.Badge;
import f5.health.app.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Schema(description = "회원 정보")
public final class MemberProfile {

    @Schema(description = "닉네임")
    private final String nickname;

    @Schema(description = "합계 포인트", example = "1200")
    private final long totalPoint;

    @Schema(description = "회원 배지")
    private final EnumModel myBadge;

    @Schema(description = "배지 리스트")
    private final List<? extends EnumModel> badgeModels;

    @Schema(description = "생일", example = "2007-05-15")
    private final LocalDate birthDate;

    @Schema(description = "성별", example = "남자")
    private final String gender;

    @Schema(description = "키", example = "170")
    private final int height;

    @Schema(description = "체중", example = "65")
    private final int weight;

    public MemberProfile(Member member, EnumModelMapper enumMapper) {
        this.nickname = member.getNickname();
        this.totalPoint = member.getTotalPoint();
        this.myBadge = new EnumModel(member.getBadge());
        this.badgeModels = enumMapper.get(Badge.class);
        this.birthDate = member.getBirthDate();
        this.gender = member.getGender().label();
        this.height = member.getHeight();
        this.weight = member.getWeight();
    }
}
