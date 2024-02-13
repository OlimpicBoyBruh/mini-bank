package ru.sberbank.jd.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Авторизация пользователя.
 */
@RestController
@RequestMapping(name = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authorization service", description = "Handles login\\logout requests.")
public class AuthController {

    @PostMapping(value = "/login")
    public void login() {

    }

    @PostMapping(value = "/logout")
    public void logout() {

    }
}
