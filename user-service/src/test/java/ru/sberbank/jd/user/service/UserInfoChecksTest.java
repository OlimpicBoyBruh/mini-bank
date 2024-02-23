package ru.sberbank.jd.user.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserInfoChecksTest {

    private UserInfoChecks checks;

    @BeforeEach
    public void setUp() {
        checks = new UserInfoChecks();
    }

    @Test
    public void testWrongFormatEmail_expectIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> checks.checkEmail("1@1"));
        assertThrows(IllegalArgumentException.class, () -> checks.checkEmail("@1.ru"));
        assertThrows(IllegalArgumentException.class, () -> checks.checkEmail("1.ru"));
    }

    @Test
    public void testGoodEmail() {
        checks.checkEmail("1@1.ru");
        checks.checkEmail("1@1.2.ru");
        checks.checkEmail("1.2@1.ru");
    }

    @Test
    public void testPhoneWithoutPlus_expectIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> checks.checkPhone("8-111-22-22"));
    }

    @Test
    public void testCorrectPhone() {
        checks.checkPhone("+7-111-111");
    }

    @Test
    public void testFutureBirthDate_expectIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> checks.checkBirthDate(
                LocalDate.now().plusDays(1)));
    }

    @Test
    public void testCorrectBirthDate() {
        checks.checkBirthDate(LocalDate.now());
        checks.checkBirthDate(LocalDate.now().minusDays(1));
    }
}
