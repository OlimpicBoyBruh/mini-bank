package ru.sberbank.jd.user.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sberbank.api.user.service.dto.UserCreateDto;
import ru.sberbank.api.user.service.dto.UserInfoDto;
import ru.sberbank.api.user.service.dto.UserUpdateDto;
import ru.sberbank.jd.user.model.UserInfo;

class UserInfoMappingTest {

    private UserInfoMapping mapper;

    @BeforeEach
    public void setUp() {
        mapper = new UserInfoMapping();
    }

    @Test
    public void testInfoWithNullPropsToDto() {
        UserInfo user = new UserInfo();

        UserInfoDto dto = mapper.mapInfoToDto(user);

        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
        assertNull(dto.getPhone());
        assertNull(dto.getEmail());
        assertNull(dto.getBirthDate());
    }

    @Test
    public void testMapFromCreateDtoWithoutPhone_expectNullPointer() {
        UserCreateDto dto = new UserCreateDto();

        assertThrows(NullPointerException.class, () -> mapper.mapDtoToInfo(dto));
    }

    @Test
    public void testNormalizePhoneOnCrate() {
        UserCreateDto dto = new UserCreateDto();
        dto.setPhone("7-111-222-333");
        dto.setPassword("test");

        UserInfo user = mapper.mapDtoToInfo(dto);

        assertEquals("7-111-222-333", user.getPhone());
        assertEquals("+7111222333", user.getPhoneNormalized());
    }

    @Test
    public void testMappingOnUpdate() {
        UserInfo user = new UserInfo();
        user.setPhone("+7-111-222-333");
        user.setPhoneNormalized("+7111222333");
        user.setEmail("old@mail");
        user.setLastName("oldLastName");
        user.setFirstName("oldFirstName");

        UserUpdateDto dto = new UserUpdateDto();
        dto.setLastName("newLastName");
        dto.setPhone("+7(333)222-111");
        dto.setEmail("new@mail");

        mapper.mapDtoToInfo(user, dto);

        assertEquals("oldFirstName", user.getFirstName());
        assertEquals("newLastName", user.getLastName());
        assertEquals("+7(333)222-111", user.getPhone());
        assertEquals("+7333222111", user.getPhoneNormalized());
    }
}
