package ru.sberbank.api;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;

/**
 * User info exchange object.
 */
@Data
public class UserInfoDto {

    private UUID id;
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
