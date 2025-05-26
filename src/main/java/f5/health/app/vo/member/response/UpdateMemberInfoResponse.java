package f5.health.app.vo.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateMemberInfoResponse {

    @Schema(description = "현재 닉네임", example = "건강짱")
    private String nickname;

    @Schema(description = "현재 키", example = "175")
    private int height;

    @Schema(description = "현재 몸무게", example = "68")
    private int weight;

    @Schema(description = "현재 주 음주량(잔)", example = "3")
    private int weekAlcoholDrinks;

    @Schema(description = "현재 하루 평균 흡연량(개비)", example = "5")
    private int daySmokeCigarettes;

    @Schema(description = "현재 주 운동 횟수", example = "4")
    private int weekExerciseFrequency;
}
