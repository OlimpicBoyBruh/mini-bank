package ru.sberbank.jd.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sberbank.jd.user.model.dto.UserInfoDto;
import ru.sberbank.jd.user.service.UserService;

/**
 * Контроллер сервиса управления данными пользователей.
 */
@RestController
@RequestMapping(value = "/user",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "User service", description = "Service for interactions with user data.")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "New user registration",
            description = "Register new user with provided user data."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "New user registered successfully",
                            content = {@Content(schema = @Schema(implementation = UserInfoDto.class))}),
                    @ApiResponse(responseCode = "400", description = "Wrong parameters supplied",
                            content = {@Content(schema = @Schema(implementation = Exception.class))})
            }
    )
    public UserInfoDto register(@RequestBody UserInfoDto dto) {
        return userService.createUser(dto);
    }

    @DeleteMapping
    @Operation(summary = "Delete registered user",
            description = "Deletes registered user and closes all accounts."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "User deleted successfully",
                            content = {@Content(schema = @Schema(implementation = UserInfoDto.class))}),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = {@Content(schema = @Schema(implementation = Exception.class))}),
                    @ApiResponse(responseCode = "500", description = "Internal error, see details",
                            content = {@Content(schema = @Schema(implementation = Exception.class))})
            }
    )
    public UserInfoDto deleteUser(@RequestParam(name = "id") String email) {
        return userService.deleteInfo(email);
    }

    @GetMapping(value = "/{id}")
    public UserInfoDto getUserInfo(@PathVariable(name = "id") UUID userId) {
        return userService.getInfo(userId);
    }
}
