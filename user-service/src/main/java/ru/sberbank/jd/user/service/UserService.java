package ru.sberbank.jd.user.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.sberbank.api.account.service.dto.AccountDto;
import ru.sberbank.api.account.service.dto.AccountNumberDto;
import ru.sberbank.api.user.service.dto.UserCreateDto;
import ru.sberbank.api.user.service.dto.UserInfoDto;
import ru.sberbank.api.user.service.dto.UserUpdateDto;
import ru.sberbank.jd.user.model.UserInfo;
import ru.sberbank.jd.user.repository.UserRepository;
import ru.sberbank.jd.user.service.rest.client.AccountClient;

/**
 * Сервис данных пользователей.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private static final String CLIENT_ID = "clientId";
    @Value("${app.account.type}")
    private String accountType;

    private final UserRepository userRepository;
    private final UserInfoMapping userInfoMapping;
    private final UserInfoChecks userInfoChecks;
    private final AccountClient accountClient;

    @Transactional
    public UserInfoDto createUser(UserCreateDto dto) {
        UserInfo user = userInfoMapping.mapDtoToInfo(dto);

        checkUser(user);
        userInfoChecks.checkBirthDate(user.getBirthDate());
        user = userRepository.save(user);

        accountClient.createAccount(accountType, user.getId().toString());

        return userInfoMapping.mapInfoToDto(user);
    }

    public UserInfoDto getInfo(UUID userId) {
        UserInfo user = findUser(userId);

        return userInfoMapping.mapInfoToDto(user);
    }

    @Transactional
    public UserInfoDto deleteInfo(UUID userId) {
        UserInfo user = findUser(userId);

        List<AccountDto> accounts = accountClient.getAccounts(userId.toString());
        List<AccountDto> deposits = accountClient.getDeposits(userId.toString());

        for (AccountDto account : accounts) {
            accountClient.deleteAccount(new AccountNumberDto(account.getNumberAccount()), userId.toString());
        }

        for (AccountDto deposit : deposits) {
            accountClient.deleteAccount(new AccountNumberDto(deposit.getNumberAccount()), userId.toString());
        }

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

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(
                    String.format("User with email %1$s already exists", user.getEmail()));
        }

        if (userRepository.existsByPhoneNormalized(user.getPhoneNormalized())) {
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
