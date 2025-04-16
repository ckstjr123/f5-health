package f5.health.app.service.auth;

import f5.health.app.jwt.JwtProvider;
import f5.health.app.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;


}
