package ru.sberbank.jd.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sberbank.jd.controller.input.ClientRequest;
import ru.sberbank.jd.entity.AccountClient;
import ru.sberbank.jd.service.AccountClientService;

/**
 * Контроллер отвечает за отображение счетов авторизированного клиента.
 * Тестовая и сырая реализация.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/profile")
public class PersonalAccountController {
    /**
     * Поле для взаимодействия с БД AccountClient.
     */
    private AccountClientService clientService;

    /**
     * Отображает все счета клиента.
     * Запрос клиента передается в body, пример {"clientId": "12345"}.
     *
     * @param clientRequest идентификатор и запрос клиента.
     * @return информацию о счетах клиента.
     */
    @GetMapping("/accounts")
    public List<AccountClient> getAccounts(@RequestBody ClientRequest clientRequest) {
        return clientService.getAccount(clientRequest.getClientId());
    }

    /**
     * Отображает все вклады клиента.
     * Запрос клиента передается в body, пример {"clientId": "12345"}.
     *
     * @return информацию о вкладах клиента.
     */
    @GetMapping("/deposits")
    public List<AccountClient> getDeposits() {
        return null;
    }
}
