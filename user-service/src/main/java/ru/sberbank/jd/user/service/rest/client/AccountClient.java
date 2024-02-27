package ru.sberbank.jd.user.service.rest.client;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;
import ru.sberbank.api.account.service.dto.AccountDto;
import ru.sberbank.api.account.service.dto.AccountNumberDto;

/**
 * Account service client proxy class.
 */
@HttpExchange(url = "/profile",
        contentType = MediaType.APPLICATION_JSON_VALUE,
        accept = MediaType.APPLICATION_JSON_VALUE)
public interface AccountClient {

    @PostExchange(url = "/open/{type}")
    AccountDto createAccount(
            @PathVariable("type") String type,
            @RequestHeader("clientId") String clientId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token);

    @PutExchange(url = "/close-account")
    AccountDto deleteAccount(
            @RequestBody AccountNumberDto account,
            @RequestHeader("clientId") String clientId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token);

    @GetExchange(url = "/deposits")
    List<AccountDto> getDeposits(
            @RequestHeader("clientId") String clientId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token);

    @GetExchange(url = "/accounts")
    List<AccountDto> getAccounts(
            @RequestHeader("clientId") String clientId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}
