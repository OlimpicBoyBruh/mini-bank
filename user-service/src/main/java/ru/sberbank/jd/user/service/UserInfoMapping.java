package ru.sberbank.jd.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;
import ru.sberbank.api.user.service.dto.UserCreateDto;
import ru.sberbank.api.user.service.dto.UserInfoDto;
import ru.sberbank.api.user.service.dto.UserUpdateDto;
import ru.sberbank.jd.user.model.UserInfo;
import ru.sberbank.jd.user.model.UserPassword;

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

        UserPassword password = new UserPassword();
        password.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder()
                .encode(dto.getPassword()));
        user.setPassword(password);

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
