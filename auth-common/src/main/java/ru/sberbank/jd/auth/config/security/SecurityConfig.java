package ru.sberbank.jd.auth.config.security;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authorization.AuthorityAuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Common security config.
 */
@Configuration()
@EnableWebSecurity
@SecurityScheme(
        name = "bearerAuth",
        in = HEADER,
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
public class SecurityConfig {

    private static final String CLIENT_ID = "clientId";

    @Autowired
    private ResourceLoader loader;

    @Value("classpath:keys/public.key")
    private RSAPublicKey publicKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/swagger-resources/**", "/swagger-ui/**",
                                "/v3/api-docs/**", "/error", "/swagger").permitAll()
                        .anyRequest()
                        .access((authorization, request) -> {
                            String clientId = request.getRequest().getHeader(CLIENT_ID);

                            if (clientId != null && !clientId.equals(authorization.get().getName())
                                    && !authorization.get().getAuthorities()
                                    .contains(new SimpleGrantedAuthority("SCOPE_ADMIN"))) {
                                return new AuthorityAuthorizationDecision(false, new ArrayList<>());
                            }

                            return new AuthorityAuthorizationDecision(true, authorization.get().getAuthorities()
                                    .stream().collect(Collectors.toList()));
                        }))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("bearerAuth")
                );
    }
}
