package ru.sberbank.jd.user.service;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 * Different checks for user information.
 */
@Component
public class UserInfoChecks {

    public void checkPhone(String phone) {
        if (!phone.startsWith("+")) {
            throw new IllegalArgumentException("Enter phone in international format (starts with \"+\"");
        }
    }

    public void checkEmail(String email) {
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Email is not valid");
        }
    }

    public void checkBirthDate(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date can't be more then today");
        }
    }
}
