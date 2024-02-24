package ru.sberbank.jd.user.service;

import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sberbank.api.user.service.dto.UserCreateDto;
import ru.sberbank.api.user.service.dto.UserInfoDto;
import ru.sberbank.api.user.service.dto.UserUpdateDto;
import ru.sberbank.jd.user.model.UserInfo;
import ru.sberbank.jd.user.repository.UserRepository;
import ru.sberbank.jd.user.service.client.AccountServiceClient;

/**
 * Сервис данных пользователей.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInfoMapping userInfoMapping;
    private final UserInfoChecks userInfoChecks;
    private final AccountServiceClient accountClient;

    @Transactional
    public UserInfoDto createUser(UserCreateDto dto) {
        UserInfo user = userInfoMapping.mapDtoToInfo(dto);

        accountClient.open();

        checkUser(user);
        userInfoChecks.checkBirthDate(user.getBirthDate());
        user = userRepository.save(user);

        return userInfoMapping.mapInfoToDto(user);
    }

    public UserInfoDto getInfo(UUID userId) {
        UserInfo user = findUser(userId);

        return userInfoMapping.mapInfoToDto(user);
    }

    @Transactional
    public UserInfoDto deleteInfo(UUID userId) {
        UserInfo user = findUser(userId);

        userRepository.delete(user);

        return userInfoMapping.mapInfoToDto(user);
    }

    @Transactional
    public UserInfoDto updateInfo(UUID userId, UserUpdateDto dto) {
        UserInfo user = findUser(userId);

        userInfoMapping.mapDtoToInfo(user, dto);
        checkUser(user);
        userRepository.save(user);

        return userInfoMapping.mapInfoToDto(user);
    }

    private void checkUser(UserInfo user) {
        userInfoChecks.checkPhone(user.getPhone());
        userInfoChecks.checkEmail(user.getEmail());

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException(
                    String.format("User with email %1$s already exists", user.getEmail()));
        }

        if (userRepository.findByPhoneNormalized(user.getPhoneNormalized()) != null) {
            throw new IllegalArgumentException(
                    String.format("User with phone %1$s already exists", user.getPhone()));
        }
    }

    private UserInfo findUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException(
                        String.format("User with id %1$s doesn't exist", userId)));
    }
}
