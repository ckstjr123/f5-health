package f5.health.app.member.vo;

import f5.health.app.common.EnumModel;
import f5.health.app.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Schema(description = "회원 프로필 응답")
public final class MemberProfile {

    @Schema(description = "닉네임")
    private final String nickname;

    @Schema(description = "합계 포인트", example = "1200")
    private final long totalPoint;

    @Schema(description = "회원 배지")
    private final EnumModel badge;

    @Schema(description = "생일", example = "2007-05-15")
    private final LocalDate birthDate;

    @Schema(description = "성별", example = "남자")
    private final String gender;

    @Schema(description = "키", example = "170")
    private final double height;

    @Schema(description = "체중", example = "65")
    private final double weight;

    public MemberProfile(Member member) {
        this.nickname = member.getNickname();
        this.totalPoint = member.getTotalPoint();
        this.badge = new EnumModel(member.getBadge());
        this.birthDate = member.getBirthDate();
        this.gender = member.getGender().label();
        this.height = member.getHeight();
        this.weight = member.getWeight();
    }
}
