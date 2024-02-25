package ru.sberbank.api.account.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO объект, содержит номер счета.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountNumberDto {

    private String accountNumber;
}
