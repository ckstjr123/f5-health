package f5.health.app.service.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "클라이언트로부터 전달된 access token이 담긴 로그인 요청 VO")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2LoginRequest {

    @Schema(description = "앱 SDK 통해 받은 액세스 토큰", example = "a1b2c3d4...", requiredMode = REQUIRED)
    @NotBlank(message = "request access token cannot be blank")
    private String accessToken;

    @Schema(description = "요청 디바이스 정보가 담긴 오브젝트", requiredMode = REQUIRED)
    @NotNull(message = "deviceInfo cannot be null")
    private DeviceInfo deviceInfo;
}
