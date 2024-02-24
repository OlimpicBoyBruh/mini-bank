package ru.sberbank.api.account.service.dto;

import java.util.List;
import lombok.Getter;

/**
 * DTO объект, содержит список счетов.
 */
@Getter
public class AccountNumbersDto {

    List<String> numberAccount;
}
