package ru.sberbank.jd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информации о переводе")
public class OperationTransferDto {

    @Schema(description = "Номер операции", required = true)
    private Long id;

    @Schema(description = "Номер счета списания средств", required = true, example = "40801810123450000001")
    private String debitAccount;

    @Schema(description = "Номер счета пополнения средств", required = true, example = "40801810123450000001")
    private String creditAccount;

    @Schema(description = "Назначение операции", required = true, example = "Пополнение счета")
    private String description;

    @DecimalMin(value = "0.0")
    @Schema(description = "Сумма операции", required = true, example = "500.00")
    @Positive(message = "Не может быть меньше нуля")
    private BigDecimal amount;
}
