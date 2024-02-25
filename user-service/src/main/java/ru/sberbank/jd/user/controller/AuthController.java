package ru.sberbank.jd.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sberbank.jd.user.service.security.CustomUserDetails;

/**
 * Авторизация пользователя.
 */
@RestController
@RequestMapping(value = "/auth")
@Tag(name = "Authorization service", description = "Handles login requests.")
@RequiredArgsConstructor
public class AuthController {

    private final JwtEncoder jwtEncoder;

    @PostMapping(value = "/login")
    @Operation(summary = "User authentication",
            description = "Authenticate user with basi authorization and returns JWT token for other requests."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "User authenticated",
                            content = {@Content(schema = @Schema(implementation = String.class))}),
                    @ApiResponse(responseCode = "401", description = "Not authorized")
            })
    public String login(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 60L;

        CustomUserDetails userDetails;
        try {
            userDetails = (CustomUserDetails) authentication.getPrincipal();
        } catch (Exception exception) {
            throw new RuntimeException("Failed to create JWT token");
        }

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("user-service")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("userId", userDetails.getUserId())
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
