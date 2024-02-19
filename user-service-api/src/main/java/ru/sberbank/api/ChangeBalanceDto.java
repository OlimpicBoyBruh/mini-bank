package ru.sberbank.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Информация о изменении баланса счета")
public class ChangeBalanceDto {
    @Schema(description = "Номер счета", required = true, example = "14010348627281")
    private String numberAccount;
    @Schema(description = "Сумма операции изменения", required = true, example = "-20.0")
    private double changeBalance;
}
