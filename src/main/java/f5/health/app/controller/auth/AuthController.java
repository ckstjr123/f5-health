package f5.health.app.controller.auth;

import f5.health.app.constant.OAuth2Provider;
import f5.health.app.service.auth.AuthService;
import f5.health.app.service.auth.vo.OAuth2LoginRequest;
import f5.health.app.service.auth.vo.SignUpRequest;
import f5.health.app.vo.auth.AuthResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApiDocs {

    private final AuthService authService;

/*
    @PostMapping("/oauth2-login/apple")
    public ResponseEntity<AuthResult> signInWithApple(@RequestBody @Valid AppleLoginRequest appleLoginRequest) {
        // example
        AuthResult authResult = new AuthResult(AuthStatus.SIGNIN, new JwtResponse("accessToken", "refreshToken"));
        return new ResponseEntity<>(authResult, authResult.httpStatus());
    }
*/

    @PostMapping("/login/oauth2/{provider}")
    public ResponseEntity<AuthResult> login(@PathVariable(name = "provider") OAuth2Provider provider,
                                            @RequestBody @Valid OAuth2LoginRequest loginRequest) {

        AuthResult loginResult = this.authService.signin(provider, loginRequest);

        return new ResponseEntity<>(loginResult, loginResult.httpStatus());
    }

    @PostMapping("/signup/oauth2/{provider}")
    public ResponseEntity<AuthResult> signup(@PathVariable(name = "provider") OAuth2Provider provider,
                                             @RequestBody @Valid SignUpRequest signUpRequest) {

        AuthResult signUpResult = this.authService.join(provider, signUpRequest);

        return new ResponseEntity<>(signUpResult, signUpResult.httpStatus());
    }

}
