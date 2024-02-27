package ru.sberbank.jd.user.service.security;

import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;

class AuthServiceTest {

    private AuthService auth;

    @BeforeEach
    public void setUt() {
        Jwt token = Mockito.mock(Jwt.class);

        JwtEncoder encoder = Mockito.mock(JwtEncoder.class);
        Mockito.when(encoder.encode(Mockito.any())).thenReturn(token);

        auth = new AuthService(encoder);
    }

    @Test
    public void testTokenCreation() {
        Jwt token = auth.login(UUID.randomUUID(), "user", "USER");

        Assertions.assertNotNull(token);
    }

}
