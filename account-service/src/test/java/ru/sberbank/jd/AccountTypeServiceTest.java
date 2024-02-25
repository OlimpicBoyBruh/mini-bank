package ru.sberbank.jd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sberbank.api.account.service.Type;
import ru.sberbank.jd.entity.AccountType;
import ru.sberbank.jd.repository.AccountTypeRepository;
import ru.sberbank.jd.service.AccountTypeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountTypeServiceTest {
    @Mock
    private AccountTypeRepository accountTypeRepository;
    @InjectMocks
    private AccountTypeService accountTypeService;

    @Test
    public void finAllAccountTypeTest() {
        List<AccountType> accountTypeList = new ArrayList<>();
        accountTypeList.add(getAccountType());
        when(accountTypeRepository.findAll()).thenReturn(accountTypeList);

        AccountType accountTypeTest = accountTypeRepository.findAll().get(0);
        assertEquals(accountTypeTest.getAccountId(), "12345");
        assertEquals(accountTypeTest.getType(), Type.DEPOSIT);
        assertEquals(accountTypeTest.getAccountName(), "Test");
    }

    @Test
    public void getAccountTest() {
        Optional<AccountType> accountType = Optional.of(getAccountType());
        when(accountTypeRepository.findById("12345")).thenReturn(accountType);
        AccountType accountTypeTest = accountTypeService.getAccount("12345");
        assertEquals(accountTypeTest.getAccountId(), "12345");
        assertEquals(accountTypeTest.getType(), Type.DEPOSIT);
        assertEquals(accountTypeTest.getAccountName(), "Test");
    }

    private AccountType getAccountType() {
        AccountType accountType = new AccountType();
        accountType.setType(Type.DEPOSIT);
        accountType.setAccountName("Test");
        accountType.setAccountId("12345");
        accountType.setInterestRate(5);
        accountType.setType(Type.DEPOSIT);
        return accountType;
    }
}