package ru.sberbank.jd.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.sberbank.api.user.service.dto.UserInfoDto;
import ru.sberbank.jd.user.model.UserInfo;
import ru.sberbank.jd.user.repository.UserRepository;

@Disabled
class UserServiceTest {

    private UserInfoMapping mapper;
    private UserInfoChecks checks;
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        UserInfoDto dto = new UserInfoDto();
        dto.setFirstName("testFirstName");
        dto.setEmail("test@mail.ru");

        mapper = Mockito.mock(UserInfoMapping.class);
        Mockito.when(mapper.mapInfoToDto(Mockito.any())).thenReturn(dto);
        Mockito.when(mapper.mapDtoToInfo(Mockito.any())).thenReturn(new UserInfo());
        Mockito.doNothing().when(mapper).mapDtoToInfo(Mockito.any(), Mockito.any());

        checks = Mockito.mock(UserInfoChecks.class);
        Mockito.doNothing().when(checks).checkEmail(Mockito.any());
        Mockito.doNothing().when(checks).checkPhone(Mockito.any());
        Mockito.doNothing().when(checks).checkBirthDate(Mockito.any());

        userRepository = Mockito.mock(UserRepository.class);
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(new UserInfo()));
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(null);
        Mockito.when(userRepository.findByPhoneNormalized(Mockito.any())).thenReturn(null);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(new UserInfo());
        Mockito.doNothing().when(userRepository).delete(Mockito.any());

//        userService = new UserService(userRepository, mapper, checks);
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
        UserInfoDto dto = userService.deleteInfo(UUID.randomUUID());

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
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(new UserInfo());

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(null));
    }

    @Test
    public void testCreateWithExitingPhone_expectIllegalArgument() {
        Mockito.when(userRepository.findByPhoneNormalized(Mockito.any())).thenReturn(new UserInfo());

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(null));
    }

    @Test
    public void testGetNotExiting_expectNoSuchElement() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getInfo(null));
    }
}
