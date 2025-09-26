package f5.health.app.session.service;

import f5.health.app.session.constant.System;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record DeviceInfo(
        @Schema(description = "flutter device_info_plus 패키지를 통해 얻은 기기 식별자", example = "7946DA4E-8429-423C-B405-B3FC77914E3E", requiredMode = REQUIRED) @NotBlank(message = "device udid cannot be blank") String udid,
        @Schema(description = "flutter device_info_plus 패키지를 통해 얻은 systemName", example = "iOS", requiredMode = REQUIRED) @NotNull(message = "device system name cannot be null") System os) {
}