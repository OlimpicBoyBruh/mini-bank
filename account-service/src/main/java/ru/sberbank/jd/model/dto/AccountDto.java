package ru.sberbank.jd.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private String numberAccount;
    private double amount;
    private String status;
    private String userId;
}
