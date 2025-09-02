package f5.health.app.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import f5.health.app.common.exception.ErrorCode;
import f5.health.app.common.exhandler.response.ExceptionResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static f5.health.app.jwt.constant.JwtConst.JWT_EXCEPTION_ATTRIBUTE;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorCode errorCode = (ErrorCode) request.getAttribute(JWT_EXCEPTION_ATTRIBUTE);
        if (errorCode != null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            this.objectMapper.writeValue(response.getWriter(), ExceptionResult.from(errorCode));
        }
    }
}
