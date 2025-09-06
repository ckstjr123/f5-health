package f5.health.app.meal.controller;

import f5.health.app.common.EnumModel;
import f5.health.app.common.exhandler.response.ExceptionResult;
import f5.health.app.auth.jwt.vo.JwtMember;
import f5.health.app.meal.service.request.MealRequest;
import f5.health.app.meal.vo.MealResponse;
import f5.health.app.meal.vo.MealsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
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


    @Operation(summary = "식단 상세 정보",
            description = "섭취한 음식 및 영양정보 등 상세 조회",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "mealId", description = "식단 아이디", required = true)
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "식사 상세 정보 응답",
                    content = @Content(schema = @Schema(implementation = MealResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "등록된 식단을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    MealResponse meal(Long mealId);


    @Operation(summary = "식단 요약 목록",
            description = "해당 일자에 기록된 식사 조회",
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "date", description = "일자", required = true)
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "식단 리스트 응답",
                    content = @Content(schema = @Schema(implementation = MealsResponse.class))
            )
    })
    MealsResponse meals(JwtMember loginMember, LocalDate date);




    @Operation(summary = "식단 등록",
            description = "아침 or 점심 or 저녁 or 간식 식사 기록",
            parameters = {
                    @Parameter(name = "mealRequest", description = "식단 기록 요청 VO", required = true,
                            content = @Content(schema = @Schema(implementation = MealRequest.class)))
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "400",
                    description = "중복된 식단 저장",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "음식을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    Long save(JwtMember loginMember, MealRequest mealRequest);
}
