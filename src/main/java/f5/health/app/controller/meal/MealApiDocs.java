package f5.health.app.controller.meal;

import f5.health.app.constant.EnumModel;
import f5.health.app.exception.response.ExceptionResult;
import f5.health.app.vo.meal.response.MealResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

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

    @Operation(summary = "식사 상세 정보",
            description = "아침 or 점심 or 저녁 or 간식 상세 정보",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "mealId", description = "식단 id", required = true)
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "해당 식사 및 식사한 음식들",
                    content = @Content(schema = @Schema(implementation = MealResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "기록된 식단 정보를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    MealResponse meal(@PathVariable Long mealId);
}
