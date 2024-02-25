package ru.sberbank.jd.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.api.account.service.dto.AccountClientDto;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;
    private final AccountServiceIntegration accountService;

    private String innerBankAccountDebit;
    private String bankUserId;

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
    public AccountClientDto closeDepositeAccount(DepositeAccountDto depDto, String userId, Jwt token) {
        if (innerBankAccountDebit == null) {
            AccountClientDto bankAccount = accountService.getBankAccount(token);
            if (bankAccount == null) {
                throw new ResourceNotFoundException("Не найден технический счет Банка для начисления процентов");
            }
            this.innerBankAccountDebit = bankAccount.getNumberAccount();
            this.bankUserId = bankAccount.getIdClient();
        }
        // получаем данные по счетам: депозитному и счету возврата
        AccountDto accountDto = checkRightToAccountAction(depDto.getDepositeAccount(), userId, token);
        checkRightToAccountAction(depDto.getReturnAccount(), userId, token);
        // вычисляем срок депозита в днях
        long depositInDaysLength = depDto.getOpeningDate().until(LocalDate.now(), ChronoUnit.DAYS);
        if (depositInDaysLength < 0) {
            throw new InvalidParamsException("Срок депозита отрицательный");
        }
        // вычисляем размер процентного дохода по депозиту
        double interestIncome = accountDto.getAmount() * depDto.getDepositeRate() / 100.0 / 365.0 * depositInDaysLength;


        doOperation(depDto.getDepositeAccount(),
                depDto.getReturnAccount(),
                BigDecimal.valueOf(accountDto.getAmount()),
                String.format("Возврат депозита № %s от %s сроком в %s дней", depDto.getDepositeAccount(),
                        depDto.getOpeningDate().toString(), depositInDaysLength),
                userId, token);

        doOperation(innerBankAccountDebit,
                depDto.getReturnAccount(),
                BigDecimal.valueOf(interestIncome),
                String.format("Начисление процентов по депозиту № %s от %s", depDto.getDepositeAccount(),
                        depDto.getOpeningDate().toString()),
                userId, token);
        AccountNumberDto accountToCloseDto = new AccountNumberDto(depDto.getDepositeAccount());
        return accountService.closeDepositeAccount(accountToCloseDto, userId, token);
    }

    private AccountDto checkRightToAccountAction(String account, String userId, Jwt token) {
        AccountDto accountDto = accountService.findByAccountId(account, userId, token);
        if (!accountDto.getUserId().equals(userId) || accountDto.getStatus().equals("CLOSE")) {
            throw new UnauthorizedOperationException(
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
