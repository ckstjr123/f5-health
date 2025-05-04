package f5.health.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import static f5.health.app.jwt.JwtProvider.ACCESS_TOKEN_TYPE;

@Configuration
public class SwaggerConfig {

    private final String SECURITY_SCHEME_NAME = ACCESS_TOKEN_TYPE + "Auth";

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION)
                .scheme(ACCESS_TOKEN_TYPE.toLowerCase())
                .bearerFormat("JWT")
                .description(ACCESS_TOKEN_TYPE + " {access_token}");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(SECURITY_SCHEME_NAME);

        return new OpenAPI()
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme))
                .addSecurityItem(securityRequirement);
    }
}