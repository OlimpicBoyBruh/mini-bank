package ru.sberbank.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeBalanceDto {
    private String numberAccount;
    private double changeBalance;
}
