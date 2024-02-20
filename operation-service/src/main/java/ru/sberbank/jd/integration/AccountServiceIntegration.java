package ru.sberbank.jd.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.sberbank.api.AccountDto;
import ru.sberbank.api.ChangeBalanceDto;
import ru.sberbank.jd.exceptions.AppError;
import ru.sberbank.jd.exceptions.ResourceNotFoundException;
import ru.sberbank.jd.exceptions.ServiceErrors;
import ru.sberbank.jd.properties.AccountServiceIntegrationProperties;

import java.rmi.ServerException;
import java.rmi.server.ServerNotActiveException;
import java.util.function.Function;

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
        Function<ClientResponse, Mono<? extends Throwable>> clientResponse4xxError = clientResponse -> clientResponse.bodyToMono(AppError.class).map(
                body -> {
                    if (body.getStatusCode().equals(ServiceErrors.NOT_FOUND.name())) {
                        log.error("Выполнен некорректный запрос к сервису счетов: счет не найден");
                        return new ResourceNotFoundException("Выполнен некорректный запрос к сервису корзин: счет не найден");
                    }
                    log.error("Выполнен некорректный запрос к сервису счетов: причина неизвестна");
                    return new ResourceNotFoundException("Выполнен некорректный запрос к сервису счетов: причина неизвестна");
                }
        );
        Function<ClientResponse, Mono<? extends Throwable>> clientResponse5xxError = clientResponse -> clientResponse.bodyToMono(AppError.class).map(
                body -> {
                    if (body.getStatusCode().equals(ServiceErrors.SERVICE_UNAVAILABLE.name())) {
                        log.error("Выполнен некорректный запрос к сервису счетов: сервис недоступен");
                        return new ServerNotActiveException("Выполнен некорректный запрос к сервису счетов: сервис недоступен");
                    }
                    log.error("Не выполнено. Внутренняя ошибка сервера.");
                    return new ServerException("Не выполнено. Внутренняя ошибка сервера.");
                }
        );
        return webClient
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(uri)
                .header("userId", userId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse4xxError
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse5xxError
                );
    }
}
