package f5.health.app.config;

import f5.health.app.jwt.JwtAuthenticationFilter;
import f5.health.app.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String[] AUTH_EXCLUDE_URIS = {
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
            , "/favicon.ico", "/signin/oauth2/{provider}", "/signup/oauth2/{provider}", "/reissue"
    };
    private final JwtProvider jwtProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(config -> config.authenticationEntryPoint(this.authenticationEntryPoint))
                .addFilterBefore(new JwtAuthenticationFilter(this.jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((registry) -> registry
                        .requestMatchers(AUTH_EXCLUDE_URIS).permitAll()
//                        .requestMatchers("/admin").hasAuthority(Role.ADMIN.name())
                        .anyRequest().authenticated());

        return http.build();
    }

}
