package ru.sberbank.jd.controller.dto;

import java.util.List;
import lombok.Getter;

/**
 * DTO объект, содержит список счетов.
 */
@Getter
public class AccountNumbersDto {
    List<String> numberAccount;
}
