package ru.sberbank.jd.service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.sberbank.api.account.service.dto.AccountDto;
import ru.sberbank.jd.entity.AccountClient;
import ru.sberbank.jd.entity.AccountType;
import ru.sberbank.api.account.service.Status;
import ru.sberbank.api.account.service.Type;
import ru.sberbank.jd.repository.AccountClientRepository;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Счет не найден"));
    }

    public AccountClient changeBalance(double change, String numberAccount) {
        AccountClient accountClient = findByNumberAccount(numberAccount);
        double amount = roundTwoDecimals(change);

        if (accountClient.getStatus().equals(Status.CLOSED)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Счет уже закрыт");
        }
        if (accountClient.getBalance() + amount < 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "На балансе недостаточно средств");
        }
        clientRepository.changeBalance(amount, numberAccount);
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
        return clientRepository.findByNumberAccountIn(accountNumbers);
    }

    public AccountDto getAccountInfo(String accountNumber) {
        Optional<AccountClient> check = clientRepository.findById(accountNumber);
        if (check.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Счет не найден");
        }
        AccountClient accountClient = check.get();

        return new AccountDto(accountNumber, accountClient.getBalance(),
                accountClient.getStatus().toString(), accountClient.getIdClient());
    }

    public void closedAccount(String accountNumber, String clientId) {
        AccountClient accountClient = findByNumberAccount(accountNumber);
        if (!accountClient.getIdClient().equals(clientId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Счет не найден");
        }
        if (accountClient.getStatus().equals(Status.CLOSED)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Счет уже закрыт");
        }
        if (accountClient.getBalance() != 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Для закрытия счета, баланс должен быть равен 0");
        }
        clientRepository.closeAccount(LocalDateTime.now(), accountNumber);
    }

    private AccountClient createAccount(String clientId, String accountId) {
        AccountType accountType = accountTypeService.getAccount(accountId);
        AccountClient accountClient = new AccountClient();
        accountClient.setNumberAccount(generateNumberAccount());
        accountClient.setOpeningDate(LocalDateTime.now());
        if (Type.DEPOSIT.equals(accountType.getType())) {
            accountClient.setClosedDate(LocalDateTime.now().plusMonths(36));
        }
        accountClient.setIdClient(clientId);
        accountClient.setStatus(Status.ACTIVE);
        accountClient.setBalance(0);
        accountClient.setType(accountType.getType());
        accountClient.setAccountType(accountType);
        return accountClient;
    }

    public double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        String formattedNumber = twoDForm.format(d).replace(",", ".");
        return Double.valueOf(formattedNumber);
    }

    private String generateNumberAccount() {
        StringBuilder number = new StringBuilder();
        number.append(4);
        Random random = new Random();
        for (int i = 0; i < 19; ++i) {
            number.append(random.nextInt(10));
        }
        return number.toString();
    }
}
