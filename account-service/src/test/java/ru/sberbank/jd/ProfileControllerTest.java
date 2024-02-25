package ru.sberbank.jd;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.sberbank.api.account.service.Status;
import ru.sberbank.api.account.service.Type;
import ru.sberbank.api.account.service.dto.AccountDto;
import ru.sberbank.api.account.service.dto.AccountNumberDto;
import ru.sberbank.api.account.service.dto.ChangeAccountBalanceDto;
import ru.sberbank.jd.controller.ProfileController;
import ru.sberbank.jd.entity.AccountClient;
import ru.sberbank.jd.entity.AccountType;
import ru.sberbank.jd.service.AccountClientService;
import ru.sberbank.jd.service.AccountTypeService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
public class ProfileControllerTest {
    @MockBean
    private AccountClientService accountClientService;
    @MockBean
    private AccountTypeService accountTypeService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void init() {
        List<AccountClient> accountClients = new ArrayList<>();
        accountClients.add(getAccountClient());
        when(accountClientService.getAccounts(anyString())).thenReturn(accountClients);
        when(accountClientService.openAccount("testClientId", "12345"))
                .thenReturn(getAccountClient());
        when(accountClientService.getAccountInfo("4004"))
                .thenReturn(new AccountDto("4004", 5, "ACTIVE", "testClientId"));
        when(accountClientService.changeBalance(5, "4004")).thenReturn(getAccountClient());
        when(accountClientService.findByNumberAccount("4004")).thenReturn(getAccountClient());
        when(accountClientService.findByNumberAccount("4004")).thenReturn(getAccountClient());
    }

    @Test
    public void getAccountsAndDepositsTest() throws Exception {
        mockMvc.perform(get("/profile/accounts").header("clientId", "testClientId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idClient").value("testClientId"));

    }

    @Test
    public void openAccountTest() throws Exception {
        mockMvc.perform(post("/profile/open/12345").header("clientId", "testClientId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idClient").value("testClientId"));


    }

    @Test
    public void getInfoAccountTest() throws Exception {
        mockMvc.perform(get("/profile/get-info/4004")).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    public void changeBalanceTest() throws Exception {
        String changeBalanceDto = mapper
                .writeValueAsString(new ChangeAccountBalanceDto("4004", 5));

        mockMvc.perform(put("/profile/change-balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changeBalanceDto)).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    public void closeAccountClientTest() throws Exception {
        AccountNumberDto accountNumberDto = new AccountNumberDto("4004");
        String accountNumber = mapper.writeValueAsString(accountNumberDto);
        mockMvc.perform(put("/profile/close-account").header("clientId", "testClientId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }


    private AccountClient getAccountClient() {
        AccountClient accountClient = new AccountClient();
        accountClient.setIdClient("testClientId");
        accountClient.setNumberAccount("4004");
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
