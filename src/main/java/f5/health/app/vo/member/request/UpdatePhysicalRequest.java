package f5.health.app.vo.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "신체 정보 수정 요청")
public class UpdatePhysicalRequest {

    @Schema(description = "키(cm)", example = "173")
    @Min(100) @Max(230)
    private int height;

    @Schema(description = "몸무게(kg)", example = "65")
    @Min(20) @Max(200)
    private int weight;
}
