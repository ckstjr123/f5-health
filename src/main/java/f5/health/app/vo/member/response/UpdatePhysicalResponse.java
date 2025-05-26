package f5.health.app.vo.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePhysicalResponse {

    @Schema(description = "수정된 키", example = "175")
    private int height;

    @Schema(description = "수정된 몸무게", example = "68")
    private int weight;

    @Schema(description = "권장 칼로리", example = "2300")
    private int recommendedCalories;
}
