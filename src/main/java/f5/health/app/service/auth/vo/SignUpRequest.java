package f5.health.app.service.auth.vo;

import f5.health.app.entity.Device.DeviceInfo;
import f5.health.app.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "신규 회원가입 요청 VO")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequest {

    @Schema(description = "회원가입 요청 정보", requiredMode = REQUIRED)
    @NotNull(message = "loginRequest cannot be null")
    @Valid
    private OAuth2LoginRequest loginRequest;

    @Schema(description = "회원 설문 결과", requiredMode = REQUIRED)
    @NotNull(message = "memberCheckUp cannot be null")
    @Valid
    private Member.MemberCheckUp memberCheckUp;

    public SignUpRequest(OAuth2LoginRequest loginRequest, Member.MemberCheckUp memberCheckUp) {
        this.loginRequest = loginRequest;
        this.memberCheckUp = memberCheckUp;
    }


    public String getAccessToken() {
        return this.loginRequest.getAccessToken();
    }

    public DeviceInfo getDeviceInfo() {
        return this.loginRequest.getDeviceInfo();
    }

}
