package f5.health.app.auth.controller;

import f5.health.app.auth.constant.OAuth2Provider;
import f5.health.app.auth.jwt.vo.JwtResponse;
import f5.health.app.auth.service.AuthService;
import f5.health.app.auth.service.vo.request.OAuth2LoginRequest;
import f5.health.app.auth.service.vo.request.SignUpRequest;
import f5.health.app.auth.vo.OAuth2LoginResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static f5.health.app.auth.jwt.constant.JwtConst.REFRESH_TOKEN_HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApiDocs {

    private final AuthService authService;

    @PostMapping("/signin/oauth2/{provider}")
    public ResponseEntity<OAuth2LoginResult> signin(@PathVariable(name = "provider") OAuth2Provider provider,
                                                    @RequestBody @Valid OAuth2LoginRequest loginRequest) {

        OAuth2LoginResult oauth2loginResult = authService.login(provider, loginRequest);

        return new ResponseEntity<>(oauth2loginResult, oauth2loginResult.httpStatus());
    }

    @PostMapping("/signup/oauth2/{provider}")
    public ResponseEntity<JwtResponse> signup(@PathVariable(name = "provider") OAuth2Provider provider,
                                              @RequestBody @Valid SignUpRequest signUpRequest) {

        JwtResponse tokenResponse = authService.join(provider, signUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse);
    }

    @PatchMapping("/refresh")
    public JwtResponse refresh(@RequestHeader(REFRESH_TOKEN_HEADER) String refreshToken) {
        return this.authService.refresh(refreshToken);
    }

}
