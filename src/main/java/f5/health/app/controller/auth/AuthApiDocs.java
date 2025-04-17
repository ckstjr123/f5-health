package f5.health.app.controller.auth;

import f5.health.app.exception.response.ExceptionResult;
import f5.health.app.service.auth.vo.AppleLoginRequest;
import f5.health.app.vo.AuthResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "인증 API", description = "인증(로그인, 회원가입, 토큰 갱신 등) 처리")
public interface AuthApiDocs {

    @Operation(summary = "애플 로그인 요청 처리",
            description = "클라이언트로부터 전달받은 Apple 토큰 검증 및 로그인 로직 수행",
            parameters = @Parameter(name = "appleLoginRequest", description = "애플 로그인 요청 VO", content = @Content(schema = @Schema(implementation = AppleLoginRequest.class)), required = true)
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "302",
                    description = "신규 회원일 경우 추가 정보를 입력받기 위해 “/signup” url로 리다이렉트"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "애플 로그인 인증 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResult.class))
            )
    })
    ResponseEntity<AuthResult> signin(AppleLoginRequest appleLoginRequest); // apple-login

    //signup~

}
