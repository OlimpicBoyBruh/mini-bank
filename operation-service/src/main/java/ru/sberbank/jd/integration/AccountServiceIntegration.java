package ru.sberbank.jd.integration;

import java.rmi.ServerException;
import java.rmi.server.ServerNotActiveException;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.sberbank.api.account.service.dto.AccountClientDto;
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
    private final String GET_BANK_ACCOUNT = "/bank-account";
    private final String BEARER = "Bearer ";

    public AccountDto findByAccountId(String id, String userId, Jwt token) {
        return getOnStatus(properties.getUrl(), ACCOUNT_INFO + id, userId, token)
                .bodyToMono(AccountDto.class)
                .block();
    }

    public AccountDto changeAccountBalance(ChangeBalanceDto changeBalanceDto, String userId, Jwt token) {
        return webClient
                .baseUrl(properties.getUrl())
                .build()
                .put()
                .uri(ACCOUNT_CHANGE_BALANCE)
                .header("userId", userId)
                .header(HttpHeaders.AUTHORIZATION, BEARER + token.getTokenValue())
                .body(Mono.just(changeBalanceDto), ChangeBalanceDto.class)
                .retrieve()
                .bodyToMono(AccountDto.class)
                .block();
    }

    private WebClient.ResponseSpec getOnStatus(String baseUrl, String uri, String userId, Jwt token) {
        return webClient
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(uri)
                .header("userId", userId)
                .header(HttpHeaders.AUTHORIZATION, BEARER + token.getTokenValue())
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

    public AccountClientDto closeDepositeAccount(AccountNumberDto accountNumberDto, String userId, Jwt token) {
        return webClient
                .baseUrl(properties.getUrl())
                .build()
                .put()
                .uri(ACCOUNT_CLOSE)
                .header("userId", userId)
                .header(HttpHeaders.AUTHORIZATION, BEARER + token.getTokenValue())
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
                .bodyToMono(AccountClientDto.class)
                .block();
    }

    public AccountClientDto getBankAccount(Jwt token) {
        return getOnStatus(properties.getUrl(), GET_BANK_ACCOUNT, "bank", token)
                .bodyToMono(AccountClientDto.class)
                .block();
    }
}
