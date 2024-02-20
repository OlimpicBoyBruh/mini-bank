package ru.sberbank.jd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информации о переводе")
public class DepositeAccountDto {

    @Schema(description = "Номер счета депозита (закрываемого)", required = true, example = "14010348627281")
    private String depositeAccount;

    @Schema(description = "Номер счета возврата депозита", required = true, example = "14010348627281")
    private String returnAccount;

    @Schema(description = "Размер ставки по депозиту", required = true, example = "15.50")
    private double depositeRate;

    @Schema(description = "Дата открытия депозита", required = true)
    private LocalDateTime openingDate;

    @Schema(description = "Дата закрытия депозита", required = true)
    private LocalDateTime closedDate;

}
