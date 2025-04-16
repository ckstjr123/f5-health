package f5.health.app.controller.auth;

import f5.health.app.constant.AuthStatus;
import f5.health.app.vo.AuthResponse;
import f5.health.app.service.auth.AuthService;
import f5.health.app.service.auth.vo.AppleLoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController implements AuthApiDocs {

    private final AuthService authService;

    @PostMapping("/apple-login")
    public AuthResponse signin(@RequestBody @Valid AppleLoginRequest appleLoginRequest) {

        // 성공 시 응답 헤더 jwt로 set ..

        return new AuthResponse(AuthStatus.SIGNIN);
    }

}
