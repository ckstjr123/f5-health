package f5.health.app.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** ENUM 반환 용도 */
@Getter
@ToString
@EqualsAndHashCode
@Schema(description = "Enum 응답 용도")
public class EnumModel {

    @Schema(description = "실제 Enum 값", example = "DINNER")
    private final String value;

    @Schema(description = "Display name", example = "저녁")
    private final String label;

    public EnumModel(MappingEnum mappingEnum) {
        this.label = mappingEnum.label();
        this.value = mappingEnum.name();
    }
}
