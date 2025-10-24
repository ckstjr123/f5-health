package f5.health.app.activity.controller;

import f5.health.app.activity.domain.AlcoholType;
import f5.health.app.activity.controller.vo.CreateActivityResponse;
import f5.health.app.activity.vo.ActivityUpdateRequest;
import f5.health.app.auth.Login;
import f5.health.app.common.vo.Date;
import f5.health.app.activity.vo.ActivityRequest;
import f5.health.app.activity.vo.ActivityResponse;
import f5.health.app.auth.vo.LoginMember;
import f5.health.app.common.EnumModel;
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
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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
                    @Parameter(name = "date", description = "활동 조회일자",
                            content = @Content(schema = @Schema(implementation = Date.class)))
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
    ActivityResponse find(LoginMember loginMember, Date date);


    @Operation(summary = "활동 기록 저장", description = "음수량 등 데이터 저장",
            parameters = {
                    @Parameter(name = "activityRequest", description = "활동 기록 요청",
                            content = @Content(schema = @Schema(implementation = ActivityRequest.class)), required = true)
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "활동 기록 완료",
                    content = @Content(schema = @Schema(implementation = CreateActivityResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "필드 유효성 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorsResult.class))
            )
    })
    ResponseEntity<CreateActivityResponse> save(LoginMember loginMember, ActivityRequest activityRequest);


    @Operation(summary = "활동 업데이트", description = "활동 기록 수정",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "activityId", description = "활동 id", required = true),
                    @Parameter(name = "activityUpdateRequest", description = "활동 업데이트 VO",
                            content = @Content(schema = @Schema(implementation = ActivityUpdateRequest.class)), required = true)
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "활동 업데이트 완료"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "필드 유효성 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorsResult.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "활동 기록을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    void updateActivity(Long activityId, LoginMember loginMember, ActivityUpdateRequest activityUpdateRequest);
    

    @Operation(summary = "음주 기록 갱신", description = "해당 주류에 대해 이미 기록된 음주 정보가 있으면 업데이트",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "activityId", description = "활동 id", required = true),
                    @Parameter(in = ParameterIn.PATH, name = "alcoholType", description = "주류", required = true),
                    @Parameter(name = "alcoholParam", description = "음주 정보 추가 요청",
                            content = @Content(schema = @Schema(implementation = ActivityRequest.AlcoholConsumptionParam.class)), required = true)
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "음주 기록 추가 완료"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "필드 유효성 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorsResult.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "활동 기록을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    void saveOrUpdateAlcoholConsumption(Long activityId, AlcoholType alcoholType, LoginMember loginMember,
                                        ActivityRequest.AlcoholConsumptionParam alcoholParam);
}
