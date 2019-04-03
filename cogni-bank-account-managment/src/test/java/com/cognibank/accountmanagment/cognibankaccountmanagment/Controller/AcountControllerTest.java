package com.cognibank.accountmanagment.cognibankaccountmanagment.Controller;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.AccountType;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.AccountService;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(AccountController.class)


public class AcountControllerTest {
    @Autowired
    private MockMvc mvc;

    @Mock
    private AccountService accountService;
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private AccountController accountController;


    @Before
    public void setup() {
        // We would need this line if we would not use MockitoJUnitRunner
        // MockitoAnnotations.initMocks(this);
        // Initializes the JacksonTester
        MockitoAnnotations.initMocks(this);
        // MockMvc standalone approach
        mvc = MockMvcBuilders
                .standaloneSetup(accountController)
                .build();
    }

    @Test
    public void createAccountControllerTest() throws Exception {
        List<Account> list = new ArrayList<>();
        Account account = new Account()
                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
                .withAccountNumber(78l)
                .withBalance(0l)
                .withUserId("12")
                .withAccountType(AccountType.Checking);

        Mockito.when(accountService.createAccount(Mockito.any(Account.class)))
                .thenReturn(account);

        mvc.perform(MockMvcRequestBuilders
                .put("/api/v1/accounts/create/{id}/{accountType}", 12, AccountType.Checking)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("0e4c1211-2c58-4956-b523-ed0d64dc54c4"));

    }

    @Test
    public void updateAccountStatusControllerTest() throws Exception {
        List<Account> list = new ArrayList<>();
        Account account = new Account()
                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
                .withAccountNumber(78l)
                .withBalance(0l)
                .withUserId("12")
                .withAccountType(AccountType.Checking);
        account.setStatus("FREEZE");

        Mockito.when(accountService.changeAccountStatus(Mockito.any(Long.class),Mockito.any(String.class)))
                .thenReturn(account.getStatus());

        mvc.perform(MockMvcRequestBuilders
                .put("/api/v1/accounts/update/{accountNumber}/{status}", 78l, "FREEZE")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("FREEZE"));

    }


}
