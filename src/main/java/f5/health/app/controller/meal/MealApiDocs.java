package f5.health.app.controller.meal;

import f5.health.app.constant.EnumModel;
import f5.health.app.exception.response.ExceptionResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "식단 API", description = "식단 조회")
public interface MealApiDocs {

    @Operation(summary = "식사 타입 리스트 조회", description = "아침/점심/저녁/간식...")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "각 MealType(식사 유형 Enum) label, value가 담긴 리스트 응답",
                    content = @Content(schema = @Schema(implementation = EnumModel.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    List<? extends EnumModel> mealTypes();

    /** mealId 식단 조회 */
}
