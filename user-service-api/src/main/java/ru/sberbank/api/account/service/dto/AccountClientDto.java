package ru.sberbank.api.account.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sberbank.api.account.service.Status;
import ru.sberbank.api.account.service.Type;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountClientDto {
    private String numberAccount;
    private String idClient;
    private double balance;
    private LocalDateTime openingDate;
    private LocalDateTime closedDate;
    private Status status;
    private Type type;
    private AccountTypeDto accountType;
}
