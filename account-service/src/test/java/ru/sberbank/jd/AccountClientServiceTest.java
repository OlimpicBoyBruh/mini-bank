package ru.sberbank.jd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sberbank.api.account.service.Status;
import ru.sberbank.api.account.service.Type;
import ru.sberbank.api.account.service.dto.AccountClientDto;
import ru.sberbank.api.account.service.dto.AccountDto;
import ru.sberbank.jd.entity.AccountClient;
import ru.sberbank.jd.entity.AccountType;
import ru.sberbank.jd.repository.AccountClientRepository;
import ru.sberbank.jd.service.AccountClientService;
import ru.sberbank.jd.service.AccountTypeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountClientServiceTest {
    @Mock
    private AccountClientRepository accountClientRepository;
    @Mock
    private AccountTypeService accountTypeService;
    @InjectMocks
    private AccountClientService accountClientService;

    @Test
    public void getAllClientTest() {
        List<AccountClient> accountClients = new ArrayList<>();
        accountClients.add(getAccountClient());
        when(accountClientRepository.findAll()).thenReturn(accountClients);
        AccountClient accountClient = accountClientService.getAllClients().get(0);
        assertEquals(accountClient.getIdClient(), "testClientId");
        assertEquals(accountClient.getNumberAccount(), "4004");
        assertEquals(accountClient.getAccountType().getAccountName(), "Test");
    }

    @Test
    public void getClientTest() {
        when(accountClientRepository.findById("12345")).thenReturn(Optional.of(getAccountClient()));
        AccountClient accountClient = accountClientService.findByNumberAccount("12345");
        assertEquals(accountClient.getIdClient(), "testClientId");
        assertEquals(accountClient.getNumberAccount(), "4004");
        assertEquals(accountClient.getAccountType().getAccountName(), "Test");
    }

    @Test
    public void getDepositsTest() {
        List<AccountClient> accountClients = new ArrayList<>();
        accountClients.add(getAccountClient());
        when(accountClientRepository.getClientDeposits("testClientId")).thenReturn(accountClients);
        List<AccountClientDto> accountClientsDto = new ArrayList<>();
        accountClientsDto = accountClientService.getDeposits("testClientId");
        AccountClient accountClient = accountClients.get(0);
        assertEquals(accountClient.getType(), Type.DEPOSIT);
    }

    @Test
    public void getAccountTest() {
        List<AccountClient> accountClients = new ArrayList<>();
        AccountClient accountClient = getAccountClient();
        accountClient.setType(Type.ACCOUNT);
        accountClients.add(accountClient);
        when(accountClientRepository.getClientDeposits("testClientId")).thenReturn(accountClients);
        List<AccountClientDto> accountClientsDto = new ArrayList<>();
        accountClientsDto = accountClientService.getDeposits("testClientId");
        accountClient = accountClients.get(0);
        assertEquals(accountClient.getType(), Type.ACCOUNT);
    }

    @Test
    public void openAccountTest() {
        when(accountTypeService.getAccount("12345")).thenReturn(getAccountType());
        AccountClient accountClient = accountClientService.openAccount("testClientId", "12345");
        assertEquals(accountClient.getIdClient(), "testClientId");
        assertEquals(accountClient.getAccountType().getAccountName(), "Test");
    }

    @Test
    public void getListAccount() {
        List<String> accountNumbers = new ArrayList<>();
        List<AccountClient> accountClients = new ArrayList<>();
        accountNumbers.add("4004");
        accountClients.add(getAccountClient());
        when(accountClientRepository.findByNumberAccountIn(accountNumbers))
                .thenReturn(accountClients);
        accountClients = accountClientService.getListAccount(accountNumbers);
        assertEquals(accountClients.get(0).getNumberAccount(), "4004");
        assertEquals(accountClients.get(0).getType(), Type.DEPOSIT);
        assertEquals(accountClients.get(0).getIdClient(), "testClientId");

    }

    @Test
    public void getAccountInfoTest() {
        when(accountClientRepository.findById("4004")).thenReturn(Optional.of(getAccountClient()));
        AccountDto accountDto = accountClientService.getAccountInfo("4004");
        assertEquals(accountDto.getNumberAccount(), "4004");
        assertEquals(accountDto.getStatus(), "ACTIVE");
        assertEquals(accountDto.getAmount(), 5);
    }

    private AccountClient getAccountClient() {
        AccountClient accountClient = new AccountClient();
        accountClient.setIdClient("testClientId");
        accountClient.setNumberAccount("4004");
        accountClient.setType(Type.DEPOSIT);
        accountClient.setAccountType(getAccountType());
        accountClient.setBalance(5);
        accountClient.setStatus(Status.ACTIVE);
        return accountClient;
    }

    private AccountType getAccountType() {
        AccountType accountType = new AccountType();
        accountType.setType(Type.DEPOSIT);
        accountType.setAccountName("Test");
        accountType.setAccountId("12345");
        accountType.setInterestRate(5);
        return accountType;
    }
}
