package ru.sberbank.jd.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.api.AccountDto;
import ru.sberbank.api.ChangeBalanceDto;
import ru.sberbank.jd.converters.OperationConverter;
import ru.sberbank.jd.dto.DepositeAccountDto;
import ru.sberbank.jd.dto.OperationTransferDto;
import ru.sberbank.jd.entities.Operation;
import ru.sberbank.jd.exceptions.InvalidParamsException;
import ru.sberbank.jd.exceptions.UnauthorizedOperationExeption;
import ru.sberbank.jd.integration.AccountServiceIntegration;
import ru.sberbank.jd.repositories.OperationRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperationService {
    private final OperationRepository operationRepository;
    private final AccountServiceIntegration accountService;

    public Optional<Operation> findById(Long id, String userId) { // +
        return operationRepository.findById(id);
    }

    public List<Operation> findOperationByAccount(String account, String userId) {
        checkRightToAccounAction(account, userId);
        return operationRepository.findByCreditAccountOrDebitAccount(account, account);
    }

    @Transactional
    public void doTransferOperation(OperationTransferDto transferDto, String userId) {

        AccountDto accountDto = checkRightToAccounAction(transferDto.getDebitAccount(), userId);

        if (transferDto.getAmount().compareTo(BigDecimal.ZERO) < 0 ||
                transferDto.getAmount().compareTo(BigDecimal.valueOf(accountDto.getAmount())) > 0) {
            throw new InvalidParamsException("Некорректная сумма перевода");
        }

        ChangeBalanceDto changeBalanceCredit = new ChangeBalanceDto(transferDto.getCreditAccount(), transferDto.getAmount().doubleValue());
        ChangeBalanceDto changeBalanceDebit = new ChangeBalanceDto(transferDto.getDebitAccount(), transferDto.getAmount().negate().doubleValue());

        Operation operation = OperationConverter.createOf(transferDto);
        accountService.changeAccountBalance(changeBalanceDebit, userId);
        accountService.changeAccountBalance(changeBalanceCredit, userId);

        operationRepository.save(operation);
    }

    public void closeDepositeAccount(DepositeAccountDto depositeAccountDto, String userId) {
    }

    private AccountDto checkRightToAccounAction(String account, String userId) {
        AccountDto accountDto = accountService.findByAccountId(account, userId);
        if (!accountDto.getUserId().equals(userId) || accountDto.getStatus().equals("CLOSE")) {
            throw new UnauthorizedOperationExeption("Доступ к счету запрещен");
        }
        return accountDto;
    }
}
