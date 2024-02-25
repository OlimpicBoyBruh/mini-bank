package ru.sberbank.jd.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.api.account.service.dto.AccountDto;
import ru.sberbank.api.account.service.dto.AccountNumberDto;
import ru.sberbank.api.account.service.dto.ChangeBalanceDto;
import ru.sberbank.api.operation.service.dto.DepositeAccountDto;
import ru.sberbank.api.operation.service.dto.OperationTransferDto;
import ru.sberbank.jd.entities.Operation;
import ru.sberbank.jd.exceptions.InvalidParamsException;
import ru.sberbank.jd.exceptions.ResourceNotFoundException;
import ru.sberbank.jd.exceptions.UnauthorizedOperationException;
import ru.sberbank.jd.integration.AccountServiceIntegration;
import ru.sberbank.jd.repositories.OperationRepository;

@Service
@RequiredArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;
    private final AccountServiceIntegration accountService;

    private final String INNER_BANK_ACCOUNT_DEBIT = "40801810123450000001";

    public Operation findById(Long id, String userId) {
        Operation operation = operationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Операция с id " + id + " не найдена"));
        checkRightToAccountAction(operation.getDebitAccount(), userId);
        return operation;
    }

    public List<Operation> findOperationByAccount(String account, String userId) {
        checkRightToAccountAction(account, userId);
        return operationRepository.findByCreditAccountOrDebitAccount(account, account);
    }

    @Transactional
    public void doTransferOperation(OperationTransferDto transferDto, String userId) {

        AccountDto accountDto = checkRightToAccountAction(transferDto.getDebitAccount(), userId);

        if (transferDto.getAmount().compareTo(BigDecimal.ZERO) < 0 ||
                transferDto.getAmount().compareTo(BigDecimal.valueOf(accountDto.getAmount())) > 0) {
            throw new InvalidParamsException("Некорректная сумма перевода");
        }

        doOperation(transferDto, userId);
    }

//    @Transactional
    public void closeDepositeAccount(DepositeAccountDto depDto, String userId) {
        // получаем данные по счетам: депозитному и счету возврата
        AccountDto accountDto = checkRightToAccountAction(depDto.getDepositeAccount(), userId);
        checkRightToAccountAction(depDto.getReturnAccount(), userId);
        // вычисляем срок депозита в днях
        long depositInDaysLength = depDto.getOpeningDate().until(LocalDate.now(), ChronoUnit.DAYS);
        if (depositInDaysLength < 0) {
            throw new InvalidParamsException("Срок депозита отрицательный");
        }
        // вычисляем размер процентного дохода по депозиту
        double interestIncome = accountDto.getAmount() * depDto.getDepositeRate() /100.0 / 365.0 * depositInDaysLength;

        doOperation(depDto.getDepositeAccount(),
                depDto.getReturnAccount(),
                BigDecimal.valueOf(accountDto.getAmount()),
                String.format("Возврат депозита № %s от %s сроком в %s дней", depDto.getDepositeAccount(),
                        depDto.getOpeningDate().toString(), depositInDaysLength),
                userId);

        doOperation(INNER_BANK_ACCOUNT_DEBIT,
                depDto.getReturnAccount(),
                BigDecimal.valueOf(interestIncome),
                String.format("Начисление процентов по депозиту № %s от %s", depDto.getDepositeAccount(),
                        depDto.getOpeningDate().toString()),
                userId);
        String s = accountService.closeDepositeAccount(new AccountNumberDto(depDto.getDepositeAccount()), userId);
        System.out.println(s);
    }

    private AccountDto checkRightToAccountAction(String account, String userId) {
        AccountDto accountDto = accountService.findByAccountId(account, userId);
        if (!accountDto.getUserId().equals(userId) || accountDto.getStatus().equals("CLOSE")) {
            throw new UnauthorizedOperationException(
                    String.format("Доступ к счету %s запрещен либо счет закрыт", account));
        }
        return accountDto;
    }

    private void doOperation(String debitAccount, String creditAccount, BigDecimal amount, String description,
                             String userId) {
        ChangeBalanceDto changeBalanceCredit = new ChangeBalanceDto(creditAccount, amount.doubleValue());
        ChangeBalanceDto changeBalanceDebit = new ChangeBalanceDto(debitAccount, amount.negate().doubleValue());
        Operation operation = new Operation(debitAccount, creditAccount, description, amount);
        accountService.changeAccountBalance(changeBalanceDebit, userId);
        accountService.changeAccountBalance(changeBalanceCredit, userId);
        operationRepository.save(operation);
    }

    private void doOperation(OperationTransferDto t, String userId) {
        doOperation(t.getDebitAccount(), t.getCreditAccount(), t.getAmount(), t.getDescription(), userId);
    }
}
