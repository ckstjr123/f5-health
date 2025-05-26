package f5.health.app.vo.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UpdateMemberInfoRequest {

    @Schema(description = "닉네임", example = "건강짱")
    private String nickname;

    @Schema(description = "키", example = "173")
    private Integer height;

    @Schema(description = "몸무게", example = "65")
    private Integer weight;

    @Schema(description = "주 음주량(잔)", example = "5")
    private Integer weekAlcoholDrinks;

    @Schema(description = "일 흡연량(개비)", example = "3")
    private Integer daySmokeCigarettes;

    @Schema(description = "주 운동 빈도", example = "3")
    private Integer weekExerciseFrequency;
}
