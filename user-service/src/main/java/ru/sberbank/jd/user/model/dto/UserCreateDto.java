package ru.sberbank.jd.user.model.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private LocalDate birthDate;
}
