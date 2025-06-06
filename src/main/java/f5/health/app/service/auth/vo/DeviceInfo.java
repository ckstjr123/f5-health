package f5.health.app.service.auth.vo;

import f5.health.app.constant.device.System;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeviceInfo {

    @Schema(description = "flutter device_info_plus 패키지를 통해 얻은 기기 식별자", example = "7946DA4E-8429-423C-B405-B3FC77914E3E", requiredMode = REQUIRED)
    @NotBlank(message = "device udid cannot be blank")
    private String udid;

    @Schema(description = "flutter device_info_plus 패키지를 통해 얻은 systemName", example = "iOS", requiredMode = REQUIRED)
    @NotNull(message = "device system name cannot be null")
    private System os;
}
