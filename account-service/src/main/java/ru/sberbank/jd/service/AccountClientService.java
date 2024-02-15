package ru.sberbank.jd.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sberbank.jd.controller.dto.AccountNumbers;
import ru.sberbank.jd.entity.AccountClient;
import ru.sberbank.jd.entity.AccountType;
import ru.sberbank.jd.repository.AccountClientRepository;
import ru.sberbank.jd.repository.AccountTypeRepository;


/**
 * Класс-сервис для взаимодействия с AccountClientRepository.
 */
@Service
@AllArgsConstructor
public class AccountClientService {
    private final AccountClientRepository clientRepository;
    private final AccountTypeService accountTypeService;

    /**
     * Запрос всех записей в Бд.
     *
     * @return list счетов.
     */
    public List<AccountClient> getAllClients() {
        return clientRepository.findAll();
    }

    /**
     * Запрос информации о всех счетах клиента.
     *
     * @param clientId идентификатор клиента.
     * @return list платежных счетов.
     */
    public List<AccountClient> getAccounts(String clientId) {
        return clientRepository.getClientAccounts(clientId);
    }
    public AccountClient findByNumberAccount(String accountNumber) {
        return clientRepository.findById(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Счет не найден"));
    }
    public AccountClient changeBalance(double change, String numberAccount) {
        AccountClient accountClient = findByNumberAccount(numberAccount);
        if (accountClient.getBalance() + change < 0) {
            throw new IllegalArgumentException("На балансе недостаточно средств");
        }
        clientRepository.changeBalance(change, numberAccount);
        return findByNumberAccount(numberAccount);
    }

    public List<AccountClient> getDeposits(String clientId) {
        return clientRepository.getClientDeposits(clientId);
    }

    public AccountClient openAccount(String clientId, String accountId) {
        AccountClient accountClient = createAccount(clientId, accountId);
        clientRepository.save(accountClient);
        return accountClient;
    }

    public List<AccountClient> getListAccount(List<String> accountNumbers) {
        return  clientRepository.findByNumberAccountIn(accountNumbers);
    }

    public void closedAccount(String numberAccount) {
        AccountClient accountClient = findByNumberAccount(numberAccount);
        if (accountClient.getClosedDate() != null && accountClient.getType().equals("Account")){
            throw new IllegalArgumentException("Данный счет уже закрыт");
        } else if (accountClient.getBalance() != 0) {
            throw new IllegalArgumentException("Для закрытия счета, баланс должен быть равен 0");
        }
        clientRepository.closeAccount(LocalDateTime.now(),numberAccount);
    }


    private AccountClient createAccount(String clientId, String accountId) {
        AccountType accountType = accountTypeService.getAccount(accountId);
        AccountClient accountClient = new AccountClient();
        accountClient.setNumberAccount(generateNumberAccount());
        accountClient.setOpeningDate(LocalDateTime.now());
        if ("Deposit".equals(accountType.getType())) {
            accountClient.setClosedDate(LocalDateTime.now().plusMonths(36));
        }
        accountClient.setIdClient(clientId);
        accountClient.setStatus("ACTIVE");
        accountClient.setBalance(0);
        accountClient.setType(accountType.getType());
        accountClient.setAccountType(accountType);
        return accountClient;
    }

    private String generateNumberAccount() {
        StringBuilder number = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; ++i) {
            number.append(random.nextInt(10));
        }
        return number.toString();
    }
}
