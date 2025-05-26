package f5.health.app.vo.member.response;

import f5.health.app.constant.EnumModel;
import f5.health.app.constant.EnumModelMapper;
import f5.health.app.constant.member.badge.Badge;
import f5.health.app.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Schema(description = "회원 정보")
public final class MemberProfile {

    @Schema(description = "닉네임")
    private final String nickname;

    @Schema(description = "합계 점수", example = "1200")
    private final long totalScore;

    @Schema(description = "회원 배지")
    private final EnumModel myBadge;

    @Schema(description = "배지 리스트(cutOffScore 포함)")
    private final List<? extends EnumModel> badgeModels;

    @Schema(description = "생일", example = "2007-05-15")
    private final LocalDate birthDate;

    @Schema(description = "성별", example = "남자")
    private final String gender;

    @Schema(description = "키", example = "170")
    private final int height;

    @Schema(description = "체중", example = "65")
    private final int weight;

    @Schema(description = "하루에 피우는 담배(0이면 비흡연자)", example = "0")
    private final int daySmokeCigarettes;

    @Schema(description = "주에 마시는 술잔", example = "0")
    private final int weekAlcoholDrinks;

    @Schema(description = "주 운동 빈도", example = "5")
    private final int weekExerciseFrequency;

    public MemberProfile(Member member, EnumModelMapper enumMapper) {
        this.nickname = member.getNickname();
        this.totalScore = member.getTotalHealthLifeScore();
        this.myBadge = new EnumModel(member.getBadge());
        this.badgeModels = enumMapper.get(Badge.class);
        this.birthDate = member.getBirthDate();
        this.gender = member.getGender().label();
        this.height = member.getHeight();
        this.weight = member.getWeight();
        this.daySmokeCigarettes = member.getDaySmokeCigarettes();
        this.weekAlcoholDrinks = member.getWeekAlcoholDrinks();
        this.weekExerciseFrequency = member.getWeekExerciseFrequency();
    }
}
