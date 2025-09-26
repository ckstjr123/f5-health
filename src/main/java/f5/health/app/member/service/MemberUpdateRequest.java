package f5.health.app.member.service;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import static f5.health.app.member.entity.Member.MAX_NICKNAME_LENGTH;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "회원 수정 요청 VO")
public record MemberUpdateRequest(
        @Schema(description = "닉네임", example = "건강짱", requiredMode = REQUIRED) @NotBlank @Length(max = MAX_NICKNAME_LENGTH) String nickname,
        @Schema(description = "키(cm)", example = "173", requiredMode = REQUIRED) @Range(min = 100, max = 230) int height,
        @Schema(description = "몸무게(kg)", example = "65", requiredMode = REQUIRED) @Range(min = 20, max = 200) int weight) {
}
