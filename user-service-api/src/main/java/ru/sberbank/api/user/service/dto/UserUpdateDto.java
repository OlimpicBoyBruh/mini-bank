package ru.sberbank.api.user.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for updating existed user.
 */
@Data
public class UserUpdateDto {

    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
    @NotBlank
    private String phone;
}
