package ru.sberbank.jd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositeAccountDto {
    private Long id;

    private String depositeAccount;
    private String returnAccount;
    private BigDecimal amount;
    private double depositeRate;

    private String description;

    private LocalDateTime openingDate;
    private LocalDateTime closedDate;

}
