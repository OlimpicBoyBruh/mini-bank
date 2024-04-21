package ru.sberbank.jd.model;

import ru.sberbank.api.account.service.dto.AccountClientDto;
import ru.sberbank.api.account.service.dto.AccountTypeDto;
import ru.sberbank.jd.entity.AccountClient;
import ru.sberbank.jd.entity.AccountType;

public class DtoConverter {
    public static AccountClientDto of(AccountClient accountClient) {
        AccountTypeDto accountTypeDto = of(accountClient.getAccountType());
        return new AccountClientDto(accountClient.getNumberAccount(), accountClient.getIdClient(),
                accountClient.getBalance(), accountClient.getOpeningDate(),
                accountClient.getClosedDate(), accountClient.getStatus(),
                accountClient.getType(), accountTypeDto);
    }

    public static AccountTypeDto of(AccountType accountType) {
        return new AccountTypeDto(accountType.getAccountId(), accountType.getAccountName(),
                accountType.getInterestRate(), accountType.isReplenishmentOption(),
                accountType.isWithdrawalOption(), accountType.getType());
    }
}
