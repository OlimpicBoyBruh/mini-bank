package ru.sberbank.jd.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sberbank.jd.entity.AccountType;
import ru.sberbank.jd.service.AccountTypeService;

/**
 * Контроллер отвечает за базовое отображение информации о доступных счетах банка.
 */
@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountInfoController {
    /**
     * Поле для взаимодействия с БД AccountType.
     */
    private AccountTypeService accountTypeService;

    /**
     * Отображает все доступные счета для открытия.
     */
    @GetMapping
    public List<AccountType> listAccountInfo() {
        return accountTypeService.getAllAccount();
    }
}
