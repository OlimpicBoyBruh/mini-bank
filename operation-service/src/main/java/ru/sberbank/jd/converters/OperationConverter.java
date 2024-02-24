package ru.sberbank.jd.converters;

import ru.sberbank.api.operation.service.dto.OperationTransferDto;
import ru.sberbank.jd.entities.Operation;

public class OperationConverter {

    public static OperationTransferDto entityToDto(Operation operation) {
        OperationTransferDto opDto = new OperationTransferDto();
        opDto.setId(operation.getId());
        opDto.setCreditAccount(operation.getCreditAccount());
        opDto.setDebitAccount(operation.getDebitAccount());
        opDto.setAmount(operation.getAmount());
        opDto.setDescription(operation.getDescription());
        return opDto;
    }

    public static Operation createOf(OperationTransferDto opDto) {
        Operation result = new Operation();
        result.setAmount(opDto.getAmount());
        result.setDescription(opDto.getDescription());
        result.setDebitAccount(opDto.getDebitAccount());
        result.setCreditAccount(opDto.getCreditAccount());
        return result;
    }
}
