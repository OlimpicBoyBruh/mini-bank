package ru.sberbank.jd.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;
import ru.sberbank.api.user.service.dto.UserCreateDto;
import ru.sberbank.api.user.service.dto.UserInfoDto;
import ru.sberbank.api.user.service.dto.UserUpdateDto;
import ru.sberbank.jd.user.service.UserService;

/**
 * Контроллер сервиса управления данными пользователей.
 */
@RequiredArgsConstructor
@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public UserInfoDto create(UserCreateDto dto, HttpServletResponse response) {
        response.setStatus(HttpStatus.CREATED.value());
        return userService.createUser(dto);
    }

    @Override
    public UserInfoDto delete(UUID userId, Jwt token) {
        return userService.deleteInfo(userId, token);
    }

    @Override
    public UserInfoDto getInfo(UUID userId) {
        return userService.getInfo(userId);
    }

    @Override
    public UserInfoDto update(UUID userId, UserUpdateDto userInfo) {
        return userService.updateInfo(userId, userInfo);
    }
}
