package ru.sberbank.api.account.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO объект, содержит счет и изменение в балансе.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeAccountBalanceDto {

    private String numberAccount;
    private double changeBalance;
}
