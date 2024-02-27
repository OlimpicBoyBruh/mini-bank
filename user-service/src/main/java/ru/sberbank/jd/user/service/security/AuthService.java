package ru.sberbank.jd.user.service.security;

import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

/**
 * Token generator for authentication.
 */
@Component
@RequiredArgsConstructor
public class AuthService {

    private final JwtEncoder jwtEncoder;
    @Value("${app.token-duration}")
    private long tokenDuration;

    public Jwt login(UUID id, String name, String authorities) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("user-service")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(tokenDuration))
                .subject(id.toString())
                .claim("userName", name)
                .claim("scope", authorities)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }
}
