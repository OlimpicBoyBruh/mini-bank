package ru.sberbank.jd.user.config.security;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorityAuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authentication. Token can be obtained in /auth/login",
        in = HEADER,
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
@SecurityScheme(
        name = "basicAuth",
        description = "Authentication to get JWT for other requests.",
        in = HEADER,
        type = HTTP,
        scheme = "basic"
)
public class SecurityConfig {

    private static final String ID = "id";
    @Value("${jwt.key.public}")
    private RSAPublicKey publicKey;
    @Value("${jwt.key.private}")
    private RSAPrivateKey privateKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .anonymous(anonymous -> anonymous.disable())
                .authorizeHttpRequests((req) -> req
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        .requestMatchers("/swagger-resources/**", "/swagger-ui/**",
                                "/v3/api-docs/**", "/error", "/swagger").permitAll()
                        .requestMatchers("/auth/login").authenticated()
                        .anyRequest()
                        .access((authorization, request) -> {
                            if (!request.getRequest().getParameter(ID)
                                    .equalsIgnoreCase(authorization.get().getName())
                                    && !authorization.get().getAuthorities()
                                    .contains(new SimpleGrantedAuthority("SCOPE_ADMIN"))) {
                                return new AuthorityAuthorizationDecision(false, new ArrayList<>());
                            }

                            return new AuthorityAuthorizationDecision(true, authorization.get().getAuthorities()
                                    .stream().collect(Collectors.toList()));
                        }))
                .httpBasic(Customizer.withDefaults())
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
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
