package ru.sberbank.jd.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.api.account.service.dto.AccountDto;
import ru.sberbank.api.account.service.dto.ChangeBalanceDto;
import ru.sberbank.api.operation.service.dto.DepositeAccountDto;
import ru.sberbank.api.operation.service.dto.OperationTransferDto;
import ru.sberbank.jd.entities.Operation;
import ru.sberbank.jd.exceptions.InvalidParamsException;
import ru.sberbank.jd.exceptions.ResourceNotFoundException;
import ru.sberbank.jd.exceptions.UnauthorizedOperationExeption;
import ru.sberbank.jd.integration.AccountServiceIntegration;
import ru.sberbank.jd.repositories.OperationRepository;

@Service
@RequiredArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;
    private final AccountServiceIntegration accountService;

    private final String INNER_BANK_ACCOUNT_DEBIT = "40801810123450000001";

    public Operation findById(Long id, String userId, Jwt token) {
        Operation operation = operationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Операция с id " + id + " не найдена"));
        checkRightToAccountAction(operation.getDebitAccount(), userId, token);
        return operation;
    }

    public List<Operation> findOperationByAccount(String account, String userId, Jwt token) {
        checkRightToAccountAction(account, userId, token);
        return operationRepository.findByCreditAccountOrDebitAccount(account, account);
    }

    @Transactional
    public void doTransferOperation(OperationTransferDto transferDto, String userId, Jwt token) {

        AccountDto accountDto = checkRightToAccountAction(transferDto.getDebitAccount(), userId, token);

        if (transferDto.getAmount().compareTo(BigDecimal.ZERO) < 0 ||
                transferDto.getAmount().compareTo(BigDecimal.valueOf(accountDto.getAmount())) > 0) {
            throw new InvalidParamsException("Некорректная сумма перевода");
        }

        doOperation(transferDto, userId, token);
    }

    @Transactional
    public void closeDepositeAccount(DepositeAccountDto depDto, String userId, Jwt token) {
        // получаем данные по счетам: депозитному и счету возврата
        AccountDto accountDto = checkRightToAccountAction(depDto.getDepositeAccount(), userId, token);
        checkRightToAccountAction(depDto.getReturnAccount(), userId, token);
        // вычисляем срок депозита в днях
        long depositInDaysLength = LocalDate.now().until(depDto.getOpeningDate(), ChronoUnit.DAYS);
        // вычисляем размер процентного дохода по депозиту
        double interestIncome = accountDto.getAmount() * depDto.getDepositeRate() / 365.0 * depositInDaysLength;

        doOperation(depDto.getDepositeAccount(),
                depDto.getReturnAccount(),
                BigDecimal.valueOf(accountDto.getAmount()),
                String.format("Возврат депозита № %s от %s", depDto.getDepositeAccount(),
                        depDto.getOpeningDate().toString()),
                userId, token);

        doOperation(INNER_BANK_ACCOUNT_DEBIT,
                depDto.getReturnAccount(),
                BigDecimal.valueOf(interestIncome),
                String.format("Начисление процентов по депозиту № %s от %s", depDto.getDepositeAccount(),
                        depDto.getOpeningDate().toString()),
                userId, token);
    }

    private AccountDto checkRightToAccountAction(String account, String userId, Jwt token) {
        AccountDto accountDto = accountService.findByAccountId(account, userId, token);
        if (!accountDto.getUserId().equals(userId) || accountDto.getStatus().equals("CLOSE")) {
            throw new UnauthorizedOperationExeption(
                    String.format("Доступ к счету %s запрещен либо счет закрыт", account));
        }
        return accountDto;
    }

    private void doOperation(String debitAccount, String creditAccount, BigDecimal amount, String description,
            String userId, Jwt token) {
        ChangeBalanceDto changeBalanceCredit = new ChangeBalanceDto(creditAccount, amount.doubleValue());
        ChangeBalanceDto changeBalanceDebit = new ChangeBalanceDto(debitAccount, amount.negate().doubleValue());
        Operation operation = new Operation(debitAccount, creditAccount, description, amount);
        accountService.changeAccountBalance(changeBalanceDebit, userId, token);
        accountService.changeAccountBalance(changeBalanceCredit, userId, token);
        operationRepository.save(operation);
    }

    private void doOperation(OperationTransferDto t, String userId, Jwt token) {
        doOperation(t.getDebitAccount(), t.getCreditAccount(), t.getAmount(), t.getDescription(), userId, token);
    }
}
