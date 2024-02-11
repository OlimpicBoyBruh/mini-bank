package ru.sberbank.jd.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sberbank.jd.entity.AccountType;
import ru.sberbank.jd.repository.AccountTypeRepository;

/**
 * Класс-сервис для взаимодействия с AccountTypeRepository.
 */
@Service
@AllArgsConstructor
public class AccountTypeService {
    private AccountTypeRepository accountTypeRepository;

    /**
     * Запрос возвращает все доступные счета для открытия.
     *
     * @return list счетов.
     */
    public List<AccountType> getAllAccount() {
        return accountTypeRepository.findAll();
    }
}
