package f5.health.app.activity.controller;

import f5.health.app.common.EnumModel;
import f5.health.app.common.exception.exhandler.response.ExceptionResult;
import f5.health.app.common.exception.exhandler.response.FieldErrorsResult;
import f5.health.app.auth.jwt.vo.JwtMember;
import f5.health.app.activity.service.request.ActivityRequest;
import f5.health.app.activity.vo.ActivityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "건강 관련 활동 기록", description = "건강 관련 기록 데이터 API")
public interface ActivityApiDocs {


    @Operation(summary = "주류 목록 조회", description = "소주, 맥주, etc.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "술 종류가 담긴 리스트 응답",
                    content = @Content(schema = @Schema(implementation = EnumModel.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    List<? extends EnumModel> alcoholTypes();

    @Operation(summary = "활동 조회", description = "해당 일자에 저장된 활동 데이터 조회",
            parameters = {
                    @Parameter(name = "recordDate", description = "활동 조회일자",
                            content = @Content(schema = @Schema(implementation = RecordDate.class)))
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "활동 기록 응답",
                    content = @Content(schema = @Schema(implementation = ActivityResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인 되지 않음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "저장된 활동 데이터 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    ActivityResponse findActivity(JwtMember loginMember, RecordDate recordDate);


    @Operation(summary = "활동 기록 저장", description = "음수량 등 데이터 저장",
            parameters = {
                    @Parameter(name = "activityRequest", description = "활동 기록 요청",
                            content = @Content(schema = @Schema(implementation = ActivityRequest.class)), required = true)
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "활동 기록 완료"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "필드 유효성 검증 실패",
                    content = @Content(schema = @Schema(implementation = FieldErrorsResult.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "연관 엔티티를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    ResponseEntity<Void> save(JwtMember loginMember, ActivityRequest activityRequest);
}
