package f5.health.app.food.controller;

import f5.health.app.food.vo.FoodResponse;
import f5.health.app.food.vo.FoodSearchResponse;
import f5.health.app.common.exception.exhandler.response.ExceptionResult;
import f5.health.app.common.exception.exhandler.response.ErrorsResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;

@Tag(name = "음식 조회 API", description = "음식 검색 & 음식 상세 정보 제공")
public interface FoodApiDocs {

    @Operation(summary = "음식 검색", description = "검색어에 부합하는 음식 검색 결과 응답")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "음식 검색 결과 리스트가 담긴 응답 객체",
                    content = @Content(schema = @Schema(implementation = FoodSearchResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "검색어가 존재하지 않음",
                    content = @Content(schema = @Schema(implementation = ErrorsResult.class))
            )
    })
    FoodSearchResponse searchFoods(@NotBlank String foodSearchQuery);


    @Operation(summary = "음식 상세 조회", description = "식품 영양 정보",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "foodId", description = "음식 id", required = true)
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "음식 조회 응답",
                    content = @Content(schema = @Schema(implementation = FoodResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인 되지 않음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당하는 음식을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    FoodResponse food(Long foodId);
}
