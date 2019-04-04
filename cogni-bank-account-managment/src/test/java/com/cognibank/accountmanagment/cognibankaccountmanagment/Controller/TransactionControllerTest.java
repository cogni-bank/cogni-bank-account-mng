package com.cognibank.accountmanagment.cognibankaccountmanagment.Controller;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.AccountType;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Transaction;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.AccountRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(TransactionController.class)


public class TransactionControllerTest {
    @Autowired
    private MockMvc mvc;

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionController transactionController;


    @Before
    public void setup() {
        // We would need this line if we would not use MockitoJUnitRunner
        // MockitoAnnotations.initMocks(this);
        // Initializes the JacksonTester
        MockitoAnnotations.initMocks(this);
        // MockMvc standalone approach
        mvc = MockMvcBuilders
                .standaloneSetup(transactionController)
                .build();
    }

    @Test
    public void depositToUserAccountTest() throws  Exception{

        Transaction newTransaction = new Transaction();

        List<Account> list = new ArrayList<>();
        Account account = new Account()
                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
                .withUserId("12")
                .withAccountNumber(78l)
                .withBalance(0l)
                .withAccountType(AccountType.Checking);

        Mockito.when(transactionService.deposit(Mockito.any(Double.class), Mockito.any(Account.class)))
                .thenReturn(account.getBalance() + newTransaction.getAmount());

        Mockito.when(accountRepository.findByAccountNumber(Mockito.anyLong()))
                .thenReturn(account);

        mvc.perform(MockMvcRequestBuilders
                .put("/api/v1/accounts/deposit/{accountNumber}/{amount}", 78l, 20.0)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(account.getUserId()));

    }

    @Test
    public void reportTest() throws  Exception{

        List<Account> list = new ArrayList<>();
        Account account = new Account()
                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
                .withUserId("12")
                .withAccountNumber(78l)
                .withBalance(0l)
                .withAccountType(AccountType.Checking);
        List<Transaction> transactionList;
        Mockito.when(transactionList=transactionService.report(Mockito.any(Long.class), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
                .thenReturn(transactionList);

        LocalDate startDate=LocalDate.now().minusDays(1l);
        LocalDate endDate=LocalDate.now().plusDays(1l);
        mvc.perform(MockMvcRequestBuilders
                .put("/api/v1/accounts/report/{accountNumber}/{startDate}/{endDate}", 78l, startDate, endDate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(transactionList.toString()));

    }

}
