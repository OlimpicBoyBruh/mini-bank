package ru.sberbank.jd.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AccountDto {
    private String numberAccount;
    private double amount;
    private String status;
    private String userId;
}
