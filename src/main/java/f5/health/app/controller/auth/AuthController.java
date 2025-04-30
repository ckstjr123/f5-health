package f5.health.app.controller.auth;

import f5.health.app.constant.OAuth2Provider;
import f5.health.app.jwt.vo.JwtResponse;
import f5.health.app.service.auth.AuthService;
import f5.health.app.service.auth.vo.OAuth2LoginRequest;
import f5.health.app.service.auth.vo.SignUpRequest;
import f5.health.app.vo.auth.OAuth2LoginResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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


    @PostMapping("/signin/oauth2/{provider}")
    public ResponseEntity<OAuth2LoginResult> signin(@PathVariable(name = "provider") OAuth2Provider provider,
                                                    @RequestBody @Valid OAuth2LoginRequest loginRequest) {

        OAuth2LoginResult oauth2loginResult = this.authService.login(provider, loginRequest);

        return new ResponseEntity<>(oauth2loginResult, oauth2loginResult.httpStatus());
    }

    @PostMapping("/signup/oauth2/{provider}")
    public ResponseEntity<JwtResponse> signup(@PathVariable(name = "provider") OAuth2Provider provider,
                                              @RequestBody @Valid SignUpRequest signUpRequest) {

        JwtResponse tokenResponse = this.authService.join(provider, signUpRequest);

        return new ResponseEntity<>(tokenResponse, HttpStatus.CREATED);
    }

//    @PostMapping("/refresh")



}
