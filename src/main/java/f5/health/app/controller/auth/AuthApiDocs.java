package f5.health.app.controller.auth;

import f5.health.app.constant.OAuth2Provider;
import f5.health.app.exception.response.ExceptionResult;
import f5.health.app.service.auth.vo.OAuth2LoginRequest;
import f5.health.app.service.auth.vo.SignUpRequest;
import f5.health.app.vo.auth.AuthResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

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
                    description = "oauth 로그인 인증 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    ResponseEntity<AuthResult> login(OAuth2Provider provider, OAuth2LoginRequest loginRequest);


    /** 회원가입 */
    @Operation(summary = "OAuth2 기반 회원가입(앱에서 id, 닉네임, 이메일 동의 항목 설정 필요)",
            description = "액세스 토큰과 회원 추가 정보 검증 후 회원가입",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "provider", description = "OAuth2 제공자", content = @Content(schema = @Schema(implementation = OAuth2Provider.class)), required = true),
                    @Parameter(name = "signUpRequest", description = "회원가입 요청 VO", content = @Content(schema = @Schema(implementation = SignUpRequest.class)), required = true)
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "401",
                    description = "oauth 회원가입 인증 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    ResponseEntity<AuthResult> signup(OAuth2Provider provider, SignUpRequest signUpRequest);

}
