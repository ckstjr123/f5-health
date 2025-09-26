package f5.health.app.auth.service.vo.request;

import f5.health.app.member.entity.vo.MemberCheckUp;
import f5.health.app.session.service.DeviceInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "신규 회원가입 요청 VO")
public record SignUpRequest(
        @Schema(description = "회원가입 요청 정보", requiredMode = REQUIRED) @NotNull(message = "loginRequest cannot be null") @Valid OAuth2LoginRequest loginRequest,
        @Schema(description = "회원 설문 결과", requiredMode = REQUIRED) @NotNull(message = "memberCheckUp cannot be null") @Valid MemberCheckUp memberCheckUp) {

    @Schema(hidden = true)
    public String accessToken() {
        return this.loginRequest.accessToken();
    }

    @Schema(hidden = true)
    public DeviceInfo deviceInfo() {
        return this.loginRequest.deviceInfo();
    }
}
