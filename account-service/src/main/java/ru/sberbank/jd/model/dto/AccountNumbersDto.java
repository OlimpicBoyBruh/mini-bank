package ru.sberbank.jd.model.dto;

import java.util.List;
import lombok.Getter;

/**
 * DTO объект, содержит список счетов.
 */
@Getter
public class AccountNumbersDto {
    List<String> numberAccount;
}
