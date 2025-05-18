package f5.health.app.controller.healthreport;

import f5.health.app.exception.response.ExceptionResult;
import f5.health.app.exception.response.FieldErrorsResult;
import f5.health.app.jwt.JwtMember;
import f5.health.app.service.healthreport.vo.request.HealthReportRequest;
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

    @Operation(summary = "리포트 조회", description = "해당 일자에 저장된 리포트 조회",
            parameters = {
                    @Parameter(name = "date", description = "조회하려는 리포트 날짜",
                            content = @Content(schema = @Schema(implementation = ReportEndDate.class)))
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "리포트 응답",
                    content = @Content(schema = @Schema(implementation = HealthReportResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인 되지 않음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "기록된 일지가 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    HealthReportResponse findReport(JwtMember loginMember, ReportEndDate date);


    @Operation(summary = "리포트 등록", description = "HealthKit, 식단, 음주/흡연량 등 데이터 처리",
            parameters = {
                    @Parameter(name = "reportRequest", description = "리포트 등록 요청",
                            content = @Content(schema = @Schema(implementation = HealthReportRequest.class)))
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "리포트 등록 완료(실제로 식단은 각 식사 타입과 총 칼로리만 응답됨)",
                    content = @Content(schema = @Schema(implementation = HealthReportResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
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
    HealthReportResponse submit(JwtMember loginMember, HealthReportRequest reportRequest);
}
