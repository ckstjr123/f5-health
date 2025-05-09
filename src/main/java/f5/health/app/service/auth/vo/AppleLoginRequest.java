/*
package f5.health.app.service.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "애플 id_token이 담긴 요청 VO")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppleLoginRequest {

    @Schema(description = "요청을 통해 받은 애플 id_token", example = "a1b2c3d4...", requiredMode = REQUIRED)
    @NotBlank(message = "apple login request id_token cannot be blank")
    private String identityToken;

    @Schema(description = "요청 디바이스 정보가 담긴 오브젝트", requiredMode = REQUIRED)
    @NotNull(message = "deviceInfo cannot be null")
    private DeviceInfo deviceInfo;

    public AppleLoginRequest(String identityToken, DeviceInfo deviceInfo) {
        this.identityToken = identityToken;
        this.deviceInfo = deviceInfo;
    }
}
*/
