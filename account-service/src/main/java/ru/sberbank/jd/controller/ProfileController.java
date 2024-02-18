package ru.sberbank.jd.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sberbank.jd.controller.dto.AccountNumbersDto;
import ru.sberbank.jd.controller.dto.ChangeAccountBalanceDto;
import ru.sberbank.jd.controller.dto.AccountNumberDto;
import ru.sberbank.jd.entity.AccountClient;
import ru.sberbank.jd.service.AccountClientService;
import ru.sberbank.jd.service.AccountTypeService;

/**
 * Контроллер отвечает за отображение счетов авторизированного клиента.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private AccountClientService clientService;
    private AccountTypeService accountService;

    /**
     * Отображает все счета клиента.
     * Запрос клиента передается header clientId.
     *
     * @param clientId идентификатор клиента.
     * @return информацию о счетах клиента.
     */

    @GetMapping("/accounts")
    public List<AccountClient> getAccounts(@RequestHeader("clientId") String clientId) {
        return clientService.getAccounts(clientId);
    }

    /**
     * Отображает все вклады клиента.
     * Запрос клиента передается в header.
     *
     * @param clientId идентификатор клиента.
     * @return информацию о вкладах клиента.
     */
    @GetMapping("/deposits")
    public List<AccountClient> getDeposits(@RequestHeader("clientId") String clientId) {
        return clientService.getDeposits(clientId);
    }

    /**
     * Отображает все вклады клиента.
     * Запрос клиента передается в header.
     *
     * @param clientId      идентификатор клиента.
     * @param accountTypeId идентификатор выбранного вклада.
     * @return информацию о новом счете.
     */

    @PostMapping("/open/{accountTypeId}")
    public AccountClient openAccount(@RequestHeader("clientId") String clientId,
                                     @PathVariable("accountTypeId") String accountTypeId) {
        return clientService.openAccount(clientId, accountTypeId);
    }

    /**
     * Запрос информации по переданным счетам.
     *
     * @param accountNumbersDto номера счетов.
     * @return информацию о счетах.
     */
    @GetMapping("/get-info")
    public List<AccountClient> getInfoAccount(@RequestBody AccountNumbersDto accountNumbersDto) {
        return clientService.getListAccount(accountNumbersDto.getNumberAccount());
    }

    /**
     * Изменение баланса у счета.
     *
     * @param changeAccountBalanceDto хранит номер счета и изменение баланса.
     * @return информацию о счетах.
     */
    @PutMapping("/change-balance")
    public AccountClient changeBalance(@RequestBody ChangeAccountBalanceDto changeAccountBalanceDto) {
        return clientService.changeBalance(changeAccountBalanceDto.getChangeBalance(),
                changeAccountBalanceDto.getNumberAccount());
    }

    /**
     * Закрывает счет.
     *
     * @param accountNumber хранит номер счета и изменение баланса.
     * @param clientId      идентификатор клиента.
     * @return информацию о счете после закрытия.
     */
    @PutMapping("/close-account")
    public AccountClient closeAccountClient(@RequestBody AccountNumberDto accountNumber,
                                            @RequestHeader("clientId") String clientId) {
        clientService.closedAccount(accountNumber.getAccountNumber(), clientId);

        return clientService.findByNumberAccount(accountNumber.getAccountNumber());
    }

}
