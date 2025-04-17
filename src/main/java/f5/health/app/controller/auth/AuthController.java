package f5.health.app.controller.auth;

import f5.health.app.constant.AuthStatus;
import f5.health.app.jwt.vo.JwtResponse;
import f5.health.app.vo.AuthResult;
import f5.health.app.service.auth.AuthService;
import f5.health.app.service.auth.vo.AppleLoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController implements AuthApiDocs {

    private final AuthService authService;

    @PostMapping("/apple-login")
    public ResponseEntity<AuthResult> signin(@RequestBody @Valid AppleLoginRequest appleLoginRequest) {

        // authService 호출..

        //example
        AuthResult authResult = new AuthResult(AuthStatus.SIGNIN, new JwtResponse("accessToken", "refreshToken"));
        return new ResponseEntity<>(authResult, authResult.httpStatus());
    }

}
