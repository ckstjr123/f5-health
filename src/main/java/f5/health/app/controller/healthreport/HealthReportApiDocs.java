package f5.health.app.controller.healthreport;

import f5.health.app.exception.auth.AuthenticationException;
import f5.health.app.exception.response.ExceptionResult;
import f5.health.app.exception.response.FieldErrorsResult;
import f5.health.app.jwt.JwtMember;
import f5.health.app.service.healthreport.vo.request.MealsRequest;
import f5.health.app.service.healthreport.vo.request.healthkit.HealthKit;
import f5.health.app.vo.healthreport.response.HealthReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "건강 일지", description = "리포트 저장 및 조회 API")
public interface HealthReportApiDocs {

    @Operation(summary = "리포트 등록", description = "HealthKit, 식단, 음주/흡연량 등 데이터 처리",
            parameters = {
                    @Parameter(name = "healthKit", description = "건강 키트", content = @Content(schema = @Schema(implementation = HealthKit.class))),
                    @Parameter(name = "mealsRequest", description = "식단 기록", content = @Content(schema = @Schema(implementation = MealsRequest.class)))
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "리포트 등록 완료",
                    content = @Content(schema = @Schema(implementation = HealthReportResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "필수 건강 데이터 누락 또는 유효성 검증 실패",
                    content = @Content(schema = @Schema(implementation = FieldErrorsResult.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "엔티티(음식 등)를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    HealthReportResponse submit(JwtMember loginMember, HealthKit healthKit, MealsRequest mealsRequest);
}
