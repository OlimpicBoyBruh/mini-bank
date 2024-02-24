package ru.sberbank.api.user.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

/**
 * DTO for creating new user.
 */
@Data
public class UserCreateDto {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
    @NotBlank
    private String phone;
    @NotNull
    private LocalDate birthDate;
    @NotNull
    private String password;
}
