package ru.sberbank.api.account.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информации о счете")
public class AccountDto {

    @Schema(description = "Номер счета", required = true, example = "40801810123450000001")
    private String numberAccount;

    @DecimalMin(value = "0.0")
    @Schema(description = "Остаток средств на счете", required = true, example = "500.00")
    @Positive(message = "Не может быть меньше нуля")
    private double amount;

    @Schema(description = "Статус счета", required = true, example = "ACTIVE")
    private String status;

    @Schema(description = "ID пользователя, владельца счета", required = true)
    private String userId;
}
