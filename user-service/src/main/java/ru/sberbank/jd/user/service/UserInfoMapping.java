package ru.sberbank.jd.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sberbank.api.UserInfoDto;
import ru.sberbank.jd.user.model.UserInfo;
import ru.sberbank.jd.user.model.dto.UserCreateDto;
import ru.sberbank.jd.user.model.dto.UserUpdateDto;

/**
 * Mappings from or to DTO.
 */
@Component
@RequiredArgsConstructor
public class UserInfoMapping {

    public UserInfoDto mapInfoToDto(UserInfo user) {
        UserInfoDto dto = new UserInfoDto();

        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setBirthDate(user.getBirthDate());

        return dto;
    }

    public UserInfo mapDtoToInfo(UserCreateDto dto) {
        UserInfo user = new UserInfo();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPhoneNormalized(normalizePhone(dto.getPhone()));
        user.setBirthDate(dto.getBirthDate());

        return user;
    }

    public void mapDtoToInfo(UserInfo user, UserUpdateDto dto) {
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPhoneNormalized(normalizePhone(dto.getPhone()));
    }

    private String normalizePhone(String phone) {
        return "+"
                + phone.replaceAll("\\D", "");
    }
}
