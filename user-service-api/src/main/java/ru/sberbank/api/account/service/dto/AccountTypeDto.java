package ru.sberbank.api.account.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sberbank.api.account.service.Type;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountTypeDto {
    private String accountId;
    private String accountName;
    private double interestRate;
    private boolean replenishmentOption;
    private boolean withdrawalOption;
    private Type type;
}
