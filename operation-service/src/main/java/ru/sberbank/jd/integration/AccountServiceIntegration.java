package ru.sberbank.jd.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountServiceIntegration {

    private final WebClient.Builder webClient;
    private final AccountServiceIntegrationProperties properties;

    private final String CHANGE_BALANCE = "/change-balance";

    public AccountDto findByAccountId(String id, String userId) {
        return getOnStatus(properties.getUrl() + "/" + id, userId)
                .bodyToMono(AccountDto.class)
                .block();
    }

    public AccountDto changeAccountBalance(ChangeBalanceDto changeBalanceDto, String userId) {
        return webClient
                .baseUrl(properties.getUrl())
                .build()
                .put()
                .uri(CHANGE_BALANCE)
                .header("userId", userId)
                .body(Mono.just(changeBalanceDto), ChangeBalanceDto.class)
                .retrieve()
                .bodyToMono(AccountDto.class)
                .block();
    }

    private WebClient.ResponseSpec getOnStatus(String url, String userId) {
        return webClient
                .baseUrl(properties.getUrl())
                .build()
                .get()
                .uri(url)
                .header("userId", userId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(AppError.class).map(
                                body -> {
                                    if (body.getStatusCode().equals(ServiceErrors.NOT_FOUND.name())) {
                                        log.error("Выполнен некорректный запрос к сервису счетов: счет не найден");
                                        return new ResourceNotFoundException("Выполнен некорректный запрос к сервису корзин: счет не найден");
                                    }
                                    log.error("Выполнен некорректный запрос к сервису счетов: причина неизвестна");
                                    return new ResourceNotFoundException("Выполнен некорректный запрос к сервису счетов: причина неизвестна");
                                }
                        )
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> clientResponse.bodyToMono(AppError.class).map(
                                body -> {
                                    if (body.getStatusCode().equals(ServiceErrors.SERVICE_UNAVAILABLE.name())) {
                                        log.error("Выполнен некорректный запрос к сервису счетов: сервис недоступен");
                                        return new ServerNotActiveException("Выполнен некорректный запрос к сервису счетов: сервис недоступен");
                                    }
                                    log.error("Не выполнено. Внутренняя ошибка сервера.");
                                    return new ServerException("Не выполнено. Внутренняя ошибка сервера.");
                                }
                        )
                );
    }
}
