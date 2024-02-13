package ru.sberbank.jd.user.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sberbank.jd.user.model.UserInfo;
import ru.sberbank.jd.user.model.dto.UserInfoDto;
import ru.sberbank.jd.user.repository.UserRepository;

/**
 * Сервис данных пользователей.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfoDto createUser(UserInfoDto dto) {
        UserInfo user = mapDtoToInfo(dto);
        userRepository.save(user);

        return dto;
    }

    public UserInfoDto getInfo(UUID id) {
        UserInfo user = userRepository.findById(id).orElseThrow();

        return mapInfoToDto(user);
    }

    public UserInfoDto deleteInfo(String email) {
        UserInfo user = userRepository.findByEmail(email);
        userRepository.delete(user);

        return mapInfoToDto(user);
    }

    public UserInfo updateInfo(UserInfoDto dto) {
        UserInfo user = userRepository.findByEmail(dto.getEmail());

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setBirthDate(dto.getBirthDate());

        return userRepository.save(user);
    }

    private UserInfoDto mapInfoToDto(UserInfo user) {
        UserInfoDto dto = new UserInfoDto();

        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setBirthDate(user.getBirthDate());

        return dto;
    }

    private UserInfo mapDtoToInfo(UserInfoDto dto) {
        UserInfo user = new UserInfo();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setBirthDate(dto.getBirthDate());

        return user;
    }
}
