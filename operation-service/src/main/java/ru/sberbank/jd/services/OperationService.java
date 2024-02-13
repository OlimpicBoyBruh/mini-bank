package ru.sberbank.jd.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sberbank.jd.dto.OperationTransferDto;
import ru.sberbank.jd.entities.Operation;
import ru.sberbank.jd.integration.AccountServiceIntegration;
import ru.sberbank.jd.repositories.OperationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperationService {
    private final OperationRepository operationRepository;
    private final AccountServiceIntegration accountService;

    public Optional<Operation> findById(Long id) {
        return operationRepository.findById(id);
    }

    public List<Operation> findOperationByAccount(Long account) {
        return new ArrayList<>();
    }
}
