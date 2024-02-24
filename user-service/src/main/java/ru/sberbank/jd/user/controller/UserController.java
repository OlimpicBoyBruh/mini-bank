package ru.sberbank.jd.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sberbank.api.user.service.dto.ErrorResponse;
import ru.sberbank.api.user.service.dto.UserCreateDto;
import ru.sberbank.api.user.service.dto.UserInfoDto;
import ru.sberbank.api.user.service.dto.UserUpdateDto;

/**
 * User controller description.
 */
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User service", description = "Service for interactions with user data.")
public interface UserController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "New user registration",
            description = "Register new user with provided user data."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "New user registered successfully",
                            content = {@Content(schema = @Schema(implementation = UserInfoDto.class))}),
                    @ApiResponse(responseCode = "400", description = "Wrong parameters supplied",
                            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
            }
    )
    UserInfoDto create(@Valid @RequestBody UserCreateDto dto, HttpServletResponse response);

    @DeleteMapping
    @Operation(summary = "Delete registered user",
            description = "Delete registered user and closes all accounts."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "User deleted successfully",
                            content = {@Content(schema = @Schema(implementation = UserInfoDto.class))}),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})}
    )
    UserInfoDto delete(@RequestParam(name = "id") UUID userId);

    @GetMapping
    @Operation(summary = "Get user information",
            description = "Return information about registered user."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Found information about user",
                            content = {@Content(schema = @Schema(implementation = UserInfoDto.class))}),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
            }
    )
    UserInfoDto getInfo(@RequestParam(name = "id") UUID userId);

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update registered user",
            description = "Updates information about registered user."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully",
                            content = {@Content(schema = @Schema(implementation = UserInfoDto.class))}),
                    @ApiResponse(responseCode = "400", description = "Wrong parameters supplied",
                            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})}
    )
    UserInfoDto update(@Valid @RequestParam(name = "id") UUID userId, @RequestBody UserUpdateDto userInfo);
}
