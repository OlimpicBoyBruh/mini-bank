package ru.sberbank.jd.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sberbank.jd.dto.OperationTransferDto;
import ru.sberbank.jd.entities.Operation;
import ru.sberbank.jd.exceptions.ResourceNotFoundException;
import ru.sberbank.jd.integration.AccountServiceIntegration;
import ru.sberbank.jd.repositories.OperationRepository;
import ru.sberbank.api.AccountDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperationService {
    private final OperationRepository operationRepository;
    private final AccountServiceIntegration accountService;

    public Optional<Operation> findById(Long id, String userId) {
        return operationRepository.findById(id);
    }

    public List<Operation> findOperationByAccount(String account, String userId) {
        //нужно получить данные по счету от сервиса счетов
        AccountDto accountDto = accountService.findByAccountId(account, userId);

        return operationRepository.findByCreditAccountOrDebitAccount(account, account);
    }

    public void doTransferOperation(OperationTransferDto transferDto, String userId) {
    }

}
