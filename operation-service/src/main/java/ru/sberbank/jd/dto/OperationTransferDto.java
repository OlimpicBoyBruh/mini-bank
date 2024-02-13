package ru.sberbank.jd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationTransferDto {

    private Long id;
    private String debitAccount;
    private String creditAccount;
    private String description;
    private BigDecimal amount;

}
