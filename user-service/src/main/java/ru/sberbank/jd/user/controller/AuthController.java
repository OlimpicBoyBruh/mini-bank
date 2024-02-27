package ru.sberbank.jd.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sberbank.jd.user.service.security.AuthService;
import ru.sberbank.jd.user.service.security.CustomUserDetails;

/**
 * Авторизация пользователя.
 */
@RestController
@RequestMapping(value = "/auth")
@Tag(name = "Authorization service", description = "Handles login requests.")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login")
    @Operation(summary = "User authentication",
            description = "Authenticate user with basic authorization and returns JWT token for other requests.",
            security = {@SecurityRequirement(name = "basicAuth")}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "User authenticated",
                            content = {@Content(schema = @Schema(implementation = String.class))}),
                    @ApiResponse(responseCode = "401", description = "Not authorized")
            })
    public String login(Authentication authentication) {
        CustomUserDetails userDetails;
        try {
            userDetails = (CustomUserDetails) authentication.getPrincipal();
        } catch (Exception exception) {
            throw new RuntimeException("Failed to create JWT token");
        }

        return authService
                .login(userDetails.getUserId(), userDetails.getUsername(), userDetails
                        .getAuthorities().stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(" ")))
                .getTokenValue();
    }
}
