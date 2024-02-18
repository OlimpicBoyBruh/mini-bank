package ru.sberbank.jd.controller.dto;

import lombok.Getter;

/**
 * DTO объект, содержит счет и изменение в балансе.
 */
@Getter
public class ChangeAccountBalanceDto {
    private String numberAccount;
    private double changeBalance;
}
