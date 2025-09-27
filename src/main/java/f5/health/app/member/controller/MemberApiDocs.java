package f5.health.app.member.controller;

import f5.health.app.common.EnumModel;
import f5.health.app.member.service.MemberUpdateRequest;
import f5.health.app.common.exception.exhandler.response.ExceptionResult;
import f5.health.app.common.exception.exhandler.response.FieldErrorsResult;
import f5.health.app.auth.jwt.vo.JwtMember;
import f5.health.app.member.vo.MemberProfile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "회원 API", description = "회원 조회/수정")
public interface MemberApiDocs {

    @Operation(summary = "배지 목록 조회", description = "모든 배지 정보 응답")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "배지 모델 리스트 응답",
                    content = @Content(schema = @Schema(implementation = EnumModel.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    List<? extends EnumModel> badges();

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