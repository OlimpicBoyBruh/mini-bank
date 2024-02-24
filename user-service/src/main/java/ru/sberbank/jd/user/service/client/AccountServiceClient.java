package ru.sberbank.jd.user.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.sberbank.api.account.service.dto.AccountDto;

/**
 * Web client for account service.
 */
@RequiredArgsConstructor
@Component
public class AccountServiceClient {

    private final WebClient.Builder client;

    public AccountDto open() {
        return client
                .build()
                .post()
                .uri("/open/ACCOUNT")
                .header("clientId", "test")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(AccountDto.class)
                .block()
                .getBody();
    }

    public AccountDto close() {
        return client
                .build()
                .post()
                .uri("/close-account")
                .header("clientId", "test")
                .body("{\"accountNumber\": \"accountNumber\"}", String.class)
                .retrieve()
                .toEntity(AccountDto.class)
                .block()
                .getBody();
    }

    public void getDeposits() {

    }

    public void getAccounts() {

    }
}
