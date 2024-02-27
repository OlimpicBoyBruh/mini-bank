package ru.sberbank.jd.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.Jwt;
import ru.sberbank.api.account.service.dto.AccountDto;
import ru.sberbank.api.user.service.dto.UserInfoDto;
import ru.sberbank.jd.user.model.UserInfo;
import ru.sberbank.jd.user.model.UserPassword;
import ru.sberbank.jd.user.repository.UserRepository;
import ru.sberbank.jd.user.service.rest.client.AccountClient;
import ru.sberbank.jd.user.service.security.AuthService;

@SpringBootTest
class UserServiceTest {

    @MockBean
    private UserInfoMapping mapper;
    @MockBean
    private UserInfoChecks checks;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AccountClient accountClient;
    @MockBean
    private AuthService authService;
    @MockBean
    private Jwt token;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        UserInfoDto dto = new UserInfoDto();
        dto.setFirstName("testFirstName");
        dto.setEmail("test@mail.ru");

        UserPassword password = new UserPassword();
        password.setPassword("test");

        UserInfo user = new UserInfo();
        user.setPassword(password);
        user.setId(UUID.randomUUID());
        user.setBirthDate(LocalDate.now());

        //mapper
        Mockito.when(mapper.mapInfoToDto(Mockito.any())).thenReturn(dto);
        Mockito.when(mapper.mapDtoToInfo(Mockito.any())).thenReturn(user);
        Mockito.doNothing().when(mapper).mapDtoToInfo(Mockito.any(), Mockito.any());

        //repository
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByEmail(Mockito.any())).thenReturn(false);
        Mockito.when(userRepository.existsByPhoneNormalized(Mockito.any())).thenReturn(false);
        Mockito.when(userRepository.saveAndFlush(Mockito.any())).thenReturn(user);
        Mockito.doNothing().when(userRepository).delete(Mockito.any());

        //external service
        Mockito.when(accountClient.getAccounts(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(new AccountDto()));
        Mockito.when(accountClient.getDeposits(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(new AccountDto()));

        //auth
        Mockito.when(authService.login(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(token);
        Mockito.when(token.getTokenValue()).thenReturn("");
    }

    @Test
    public void testCreate() {
        UserInfoDto dto = userService.createUser(null);

        assertEquals("testFirstName", dto.getFirstName());
        assertEquals("test@mail.ru", dto.getEmail());
    }

    @Test
    public void testGet() {
        UserInfoDto dto = userService.getInfo(UUID.randomUUID());

        assertEquals("testFirstName", dto.getFirstName());
        assertEquals("test@mail.ru", dto.getEmail());
    }

    @Test
    public void testDelete() {
        UserInfoDto dto = userService.deleteInfo(UUID.randomUUID(), token);

        assertEquals("testFirstName", dto.getFirstName());
        assertEquals("test@mail.ru", dto.getEmail());
    }

    @Test
    public void testUpdate() {
        UserInfoDto dto = userService.updateInfo(UUID.randomUUID(), null);

        assertEquals("testFirstName", dto.getFirstName());
        assertEquals("test@mail.ru", dto.getEmail());
    }

    @Test
    public void testCreateWithExitingEmail_expectIllegalArgument() {
        Mockito.when(userRepository.existsByEmail(Mockito.any())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(null));
    }

    @Test
    public void testCreateWithExitingPhone_expectIllegalArgument() {
        Mockito.when(userRepository.existsByPhoneNormalized(Mockito.any())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(null));
    }

    @Test
    public void testGetNotExiting_expectNoSuchElement() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getInfo(null));
    }
}
