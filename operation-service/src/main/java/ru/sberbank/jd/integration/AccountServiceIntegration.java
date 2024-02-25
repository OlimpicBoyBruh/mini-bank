package ru.sberbank.jd.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.sberbank.api.account.service.dto.AccountDto;
import ru.sberbank.api.account.service.dto.AccountNumberDto;
import ru.sberbank.api.account.service.dto.ChangeBalanceDto;
import ru.sberbank.jd.exceptions.*;
import ru.sberbank.jd.properties.AccountServiceIntegrationProperties;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountServiceIntegration {

    private final WebClient.Builder webClient;
    private final AccountServiceIntegrationProperties properties;

    private final String ACCOUNT_CHANGE_BALANCE = "/change-balance";
    private final String ACCOUNT_INFO = "/get-info/"; // /get-info/{accountNumber}
    private final String ACCOUNT_CLOSE = "/close-account";

    public AccountDto findByAccountId(String id, String userId) {
        return getOnStatus(properties.getUrl(), ACCOUNT_INFO + id, userId)
                .bodyToMono(AccountDto.class)
                .block();
    }

    public AccountDto changeAccountBalance(ChangeBalanceDto changeBalanceDto, String userId) {
        return webClient
                .baseUrl(properties.getUrl())
                .build()
                .put()
                .uri(ACCOUNT_CHANGE_BALANCE)
                .header("userId", userId)
                .body(Mono.just(changeBalanceDto), ChangeBalanceDto.class)
                .retrieve()
                .bodyToMono(AccountDto.class)
                .block();
    }

    private WebClient.ResponseSpec getOnStatus(String baseUrl, String uri, String userId) {
        return webClient
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(uri)
                .header("userId", userId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        error -> Mono.error(new ResourceNotFoundException("Выполнен некорректный запрос к сервису счетов: счет не найден"))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        error -> Mono.error(new InvalidParamsException("Выполнен некорректный запрос к сервису счетов: сервис недоступен"))
                );
    }

    public String closeDepositeAccount(AccountNumberDto accountNumberDto, String userId) {
        return webClient
                .baseUrl(properties.getUrl())
                .build()
                .put()
                .uri(ACCOUNT_CLOSE)
                .header("userId", userId)
                .body(Mono.just(accountNumberDto), AccountNumberDto.class)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        error -> Mono.error(new ResourceNotFoundException("Выполнен некорректный запрос к сервису счетов: счет не найден либо баланс не нулевой"))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        error -> Mono.error(new InvalidParamsException("Выполнен некорректный запрос к сервису счетов: сервис недоступен"))
                )
                .bodyToMono(String.class)
                .block();
    }
}
