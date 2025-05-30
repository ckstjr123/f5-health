package f5.health.app.entity.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import static f5.health.app.entity.member.Member.*;
import static f5.health.app.entity.member.Member.MemberCheckUp.WEEK_MAX_ALCOHOL_ML;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Schema(description = "회원 수정 요청 VO")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberUpdateRequest {

    @Schema(description = "닉네임", example = "건강짱", requiredMode = REQUIRED)
    @NotBlank
    @Length(max = MAX_NICKNAME_LENGTH)
    private String nickname;

    @Schema(description = "키(cm)", example = "173", requiredMode = REQUIRED)
    @Range(min = 100, max = 230)
    private int height;

    @Schema(description = "몸무게(kg)", example = "65", requiredMode = REQUIRED)
    @Range(min = 20, max = 200)
    private int weight;

    @Schema(description = "일일 흡연량(개비)", example = "3", requiredMode = REQUIRED)
    @Range(min = 0, max = 30)
    private int daySmokeCigarettes;

    @Schema(description = "주 음주량(ml)", example = "1200", requiredMode = REQUIRED)
    @Range(min = 0, max = WEEK_MAX_ALCOHOL_ML)
    private int weekAlcoholDrinks;

    @Schema(description = "주 운동 빈도", example = "3", requiredMode = REQUIRED)
    @Range(min = 0, max = DAYS_IN_WEEK)
    private int weekExerciseFrequency;
}
