package ru.sberbank.jd.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.jd.controller.dto.AccountNumbers;
import ru.sberbank.jd.controller.dto.ChangeAccountBalance;
import ru.sberbank.jd.controller.dto.NumberAccount;
import ru.sberbank.jd.entity.AccountClient;
import ru.sberbank.jd.service.AccountClientService;
import ru.sberbank.jd.service.AccountTypeService;

/**
 * Контроллер отвечает за отображение счетов авторизированного клиента.
 * Тестовая и сырая реализация.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    /**
     * Поле для взаимодействия с БД AccountClient.
     */
    private AccountClientService clientService;
    private AccountTypeService accountService;

    /**
     * Отображает все счета клиента.
     * Запрос клиента передается в body, пример {"clientId": "12345"}.
     *
     * @param clientId идентификатор и запрос клиента.
     * @return информацию о счетах клиента.
     */
    @GetMapping("/accounts")
    public List<AccountClient> getAccounts(@RequestHeader("clientId") String clientId) {
        return clientService.getAccounts(clientId);
    }

    /**
     * Отображает все вклады клиента.
     * Запрос клиента передается в body, пример {"clientId": "12345"}.
     *
     * @return информацию о вкладах клиента.
     */
    @GetMapping("/deposits")
    public List<AccountClient> getDeposits(@RequestHeader("clientId") String clientId) {
        return clientService.getDeposits(clientId);
    }

    @PostMapping("/open/{accountTypeId}")
    public AccountClient openAccount(@RequestHeader("clientId") String clientId,
                                     @PathVariable("accountTypeId") String accountId) {
        return clientService.openAccount(clientId, accountId);
    }

    @GetMapping("/get-info")
    public List<AccountClient> getInfoAccount(@RequestBody AccountNumbers accountNumbers) {
        return clientService.getListAccount(accountNumbers.getNumberAccount());
    }

    @PutMapping("/change-balance")
    public AccountClient changeBalance(@RequestBody ChangeAccountBalance changeAccountBalance) {
        System.out.println(changeAccountBalance.getChangeBalance());
        return clientService.changeBalance(changeAccountBalance.getChangeBalance(),
                changeAccountBalance.getNumberAccount());
    }

    @PutMapping("/close-account")
    public AccountClient closeAccountClient(@RequestBody NumberAccount accountNumber) {
        clientService.closedAccount(accountNumber.getNumberAccount());
        return clientService.findByNumberAccount(accountNumber.getNumberAccount());
    }

}
