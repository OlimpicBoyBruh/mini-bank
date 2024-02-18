package ru.sberbank.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private String numberAccount;
    private double amount;
    private String status;
    private String userId;
}
