package f5.health.app.controller.food;

import f5.health.app.exception.response.ExceptionResult;
import f5.health.app.exception.response.FieldErrorsResult;
import f5.health.app.jwt.JwtMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
                    content = @Content(schema = @Schema(implementation = FieldErrorsResult.class))
            )
    })
    FoodSearchResponse searchFoods(@NotBlank String foodSearchQuery);


    @Operation(summary = "음식 상세 조회", description = "식품 영양 정보",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "foodCode", description = "식품 코드(PK)", required = true)
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
                    description = "해당하는 식품 코드를 가진 음식을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    FoodResponse food(String foodCode);


    @Operation(summary = "음식 상세 조회 리스트", description = "로컬에서 식단 임시 저장 시 식사한 음식들 상세 정보 조회",
            parameters = {
                    @Parameter(name = "eatenMealFoodsRequest", description = "먹은 음식들 요청",
                            content = @Content(schema = @Schema(implementation = EatenMealFoodsRequest.class)))
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "요청 사용자 권장 칼로리 및 음식 리스트 응답",
                    content = @Content(schema = @Schema(implementation = FoodsResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "음식 코드 형식 오류",
                    content = @Content(schema = @Schema(implementation = FieldErrorsResult.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인 되지 않음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    FoodsResponse foods(JwtMember loginMember, @Valid EatenMealFoodsRequest eatenMealFoodsRequest);
}
