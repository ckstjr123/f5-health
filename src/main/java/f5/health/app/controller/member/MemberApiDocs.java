package f5.health.app.controller.member;

import f5.health.app.entity.member.MemberUpdateRequest;
import f5.health.app.exception.response.ExceptionResult;
import f5.health.app.exception.response.FieldErrorsResult;
import f5.health.app.jwt.JwtMember;
import f5.health.app.vo.member.response.MemberProfile;
import f5.health.app.vo.member.response.MemberSavings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "회원 API", description = "회원 조회/수정")
public interface MemberApiDocs {

    @Operation(summary = "회원 프로필", description = "내 정보(MY)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 정보 응답",
                    content = @Content(schema = @Schema(implementation = MemberProfile.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인 되지 않음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "회원을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    MemberProfile profile(JwtMember loginMember);


    @Operation(summary = "회원 절약 금액", description = "절약 금액 관련 데이터 응답")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "음주/흡연 절약 금액 및 AI 건강 물품 추천",
                    content = @Content(schema = @Schema(implementation = MemberSavings.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인 되지 않음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "회원을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    MemberSavings savings(JwtMember loginMember);


    @Operation(summary = "회원 정보 수정", description = "회원 프로필 조회 후 수정(수정 허용 필드를 제외하고 블락 처리)",
            parameters = {
                    @Parameter(name = "updateParam", description = "회원 수정 파라미터",
                            content = @Content(schema = @Schema(implementation = MemberUpdateRequest.class)), required = true)
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 수정 완료"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "입력 값이 올바르지 않음",
                    content = @Content(schema = @Schema(implementation = FieldErrorsResult.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인 되지 않음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "회원을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    void edit(JwtMember loginMember, MemberUpdateRequest updateParam);
}