package f5.health.app.auth.controller;

import f5.health.app.auth.constant.OAuth2Provider;
import f5.health.app.common.exception.exhandler.response.ExceptionResult;
import f5.health.app.auth.jwt.vo.JwtResponse;
import f5.health.app.auth.service.vo.request.OAuth2LoginRequest;
import f5.health.app.auth.service.vo.request.SignUpRequest;
import f5.health.app.auth.vo.OAuth2LoginResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import static f5.health.app.auth.jwt.constant.JwtConst.REFRESH_TOKEN_HEADER;

@Tag(name = "인증 관련 API", description = "인증(로그인, 회원가입, 토큰 갱신 등) 처리")
public interface AuthApiDocs {

    /** 로그인 */
    @Operation(summary = "OAuth2 기반 로그인 요청 처리(앱에서 id, 닉네임, 이메일 동의 항목 설정 필요)",
            description = "액세스 토큰 검증 및 로그인 로직 수행",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "provider", description = "OAuth2 제공자", content = @Content(schema = @Schema(implementation = OAuth2Provider.class)), required = true),
                    @Parameter(name = "loginRequest", description = "로그인 요청 VO", content = @Content(schema = @Schema(implementation = OAuth2LoginRequest.class)), required = true)
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "302",
                    description = "신규 회원이므로 회원 추가 정보를 받기 위해 “/signup” url 요청 필요"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "OAuth 로그인 인증 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    ResponseEntity<OAuth2LoginResult> login(OAuth2Provider provider, OAuth2LoginRequest loginRequest);


    /** 회원가입 */
    @Operation(summary = "OAuth2 기반 회원가입(앱에서 id, 닉네임, 이메일 동의 항목 설정 필요)",
            description = "액세스 토큰과 회원 추가 정보 검증 후 회원가입",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "provider", description = "OAuth2 제공자", content = @Content(schema = @Schema(implementation = OAuth2Provider.class)), required = true),
                    @Parameter(name = "signUpRequest", description = "회원가입 VO", content = @Content(schema = @Schema(implementation = SignUpRequest.class)), required = true)
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "OAuth2 회원가입 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    ResponseEntity<JwtResponse> signup(OAuth2Provider provider, SignUpRequest signUpRequest);


    /** 토큰 재발급 */
    @Operation(summary = "접근 & 갱신 토큰 재발급",
            description = "갱신 토큰 검증 뒤 토큰 재발급 처리",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = REFRESH_TOKEN_HEADER, description = "갱신 토큰 헤더", required = true)
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "토큰 재발급 성공"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "만료되었거나 부적절한 갱신 토큰이므로 검증 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    JwtResponse refresh(String refreshToken);
}
