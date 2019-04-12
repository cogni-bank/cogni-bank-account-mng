package com.cognibank.accountmanagment.cognibankaccountmanagment.Controller;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.*;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(AccountController.class)

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
        List<Account> list = new ArrayList<Account>();
        Account account = new Account()
                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
                .withAccountNumber(78l)
                .withBalance(0l)
                .withUserId("12")
                .withAccountType(AccountType.Checking);

        Mockito.when(accountService.createAccount(Mockito.any(Account.class)))
                .thenReturn(account);

        mvc.perform(MockMvcRequestBuilders
                .put("/users/accounts/create/{id}/{accountType}", 12, AccountType.Checking)
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
                .put("/users/accounts/update/{accountNumber}/{status}", 78l, "FREEZE")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("FREEZE"));

    }


    @Test
    public void getUserAccountsTest() throws Exception {
        List<Account> list = new ArrayList<>();
        Account checkingAccount = new Account()
                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
                .withAccountNumber(78l)
                .withBalance(0l)
                .withUserId("12")
                .withAccountType(AccountType.Checking);

        Account savingsAccount = new Account()
                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
                .withAccountNumber(78l)
                .withBalance(0l)
                .withUserId("12")
                .withAccountType(AccountType.Savings);
        List<Account> accountList=new
                ArrayList<>();
        accountList.add(checkingAccount); accountList.add(savingsAccount);
        Mockito.when(accountService.getUserAccounts(Mockito.any(String.class)))
                .thenReturn(accountList);
        String expected="[{\"id\":\"0e4c1211-2c58-4956-b523-ed0d64dc54c4\",\"accountNumber\":78,\"accountType\":\"Checking\",\"status\":\"ACTIVE\",\"balance\":0.0,\"userId\":\"12\"}," +
                "{\"id\":\"0e4c1211-2c58-4956-b523-ed0d64dc54c4\",\"accountNumber\":78,\"accountType\":\"Savings\",\"status\":\"ACTIVE\",\"balance\":0.0,\"userId\":\"12\"}]";
        mvc.perform(MockMvcRequestBuilders
                .put("/users/accounts/accountsList/{userId}","12")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expected));

    }
}
