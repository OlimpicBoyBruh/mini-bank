package ru.sberbank.jd.converters;

import ru.sberbank.jd.dto.OperationTransferDto;
import ru.sberbank.jd.entities.Operation;

public class OperationConverter {
    public static OperationTransferDto entityToDto(Operation operation) {
        OperationTransferDto opDto = new OperationTransferDto();
        // присвоение полей
        return opDto;

    }
}
