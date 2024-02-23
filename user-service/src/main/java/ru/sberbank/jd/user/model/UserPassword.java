package ru.sberbank.jd.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

/**
 * Пароль пользователя.
 */
@Data
@Embeddable
public class UserPassword {

    @Column(name = "password")
    private String password;
}
