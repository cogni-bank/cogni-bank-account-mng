package com.cognibank.accountmanagment.cognibankaccountmanagment.Controller;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions.LowBalanceException;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.*;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.AccountRepository;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.AccountService;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.TransactionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(TransactionController.class)

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionControllerTest {
    @Autowired
    private MockMvc mvc;

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionController transactionController;

    @Autowired
    AccountService accountService;

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
    public void depositToUserAccountTest() throws Exception {

        Transaction newTransaction = new Transaction();

        //List<Account> list = new ArrayList<>();
        Account account = new Account()
                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
                .withUserId("12")
                .withAccountNumber(78l)
                .withBalance(0l)
                .withAccountType(AccountType.Checking);

        Mockito.when(transactionService.deposit(Mockito.any(Double.class), Mockito.any(Account.class)))
                .thenReturn(account.getBalance() + newTransaction.getAmount());

        Mockito.when(accountRepository.findByAccountNumber(Mockito.anyLong())).thenReturn(account);

        mvc.perform(MockMvcRequestBuilders
                .put("/users/accounts/deposit/{accountNumber}/{amount}", 78l, 20.0)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(account.getUserId()));
    }
    @Test
    public void getAccountByUserIdTest() throws Exception {

        Transaction newTransaction = new Transaction();

       // List<Account> list = new ArrayList<>();
        Optional<Account> account = Optional.of(new Account()
                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
                .withUserId("12")
                .withAccountNumber(78l)
                .withBalance(0l)
                .withAccountType(AccountType.Checking));

        Mockito.when(accountRepository.findById(Mockito.anyString())).thenReturn(account);

        mvc.perform(MockMvcRequestBuilders
                .put("/users/accounts/getAccountNumberById/{accountId}", 78l)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(account.get().getAccountNumber()+""));
    }
    @Test
    public void withdrawToUserAccountTest() throws Exception {

        Transaction newTransaction = new Transaction();

        //List<Account> list = new ArrayList<>();
        Account account = new Account()
                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
                .withUserId("12")
                .withAccountNumber(78l)
                .withBalance(0l)
                .withAccountType(AccountType.Savings);

        Mockito.when(transactionService.withdraw(Mockito.any(Double.class), Mockito.any(Account.class)))
                .thenReturn(account.getBalance() - newTransaction.getAmount());

        Mockito.when(accountRepository.findByAccountNumber(Mockito.anyLong()))
                .thenReturn(account);

        mvc.perform(MockMvcRequestBuilders
                .put("/users/accounts/withdraw/{accountNumber}/{amount}", 78l, 20.0)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(account.getUserId()));
    }

    @Test
    public void transferTestToUserAccountTest() throws Exception {

        Account account = new Account()
                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
                .withUserId("12")
                .withAccountNumber(78l)
                .withBalance(100)
                .withAccountType(AccountType.Checking);
        Account destinationAccount = new Account()
                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
                .withUserId("13")
                .withAccountNumber(79l)
                .withBalance(34)
                .withAccountType(AccountType.Checking);

        Transaction newTransaction = new Transaction().setId(1);
        newTransaction.setDestinationAccount(destinationAccount.getId());
        newTransaction.setAccount(account);
        newTransaction.setAmount(50.0);



        Mockito.when(transactionService.transfer(Mockito.any(Long.class), Mockito.any(Long.class),Mockito.anyDouble()))
                .thenReturn(newTransaction.getId());

        mvc.perform(MockMvcRequestBuilders
                .put("/users/accounts/transfer/{originAccountNumber}/{destinationAccountNumber}/{amount}", newTransaction.getAccount().getAccountNumber(), destinationAccount.getAccountNumber(),50.0)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(newTransaction.getId()+""));
    }
    @Test
    //return result in xml format
    public void withdrawToUserXMLAccountTest() throws Exception {

        Transaction newTransaction = new Transaction();

        List<Account> list = new ArrayList<>();
        Account account = new Account()
                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
                .withUserId("12")
                .withAccountNumber(78l)
                .withBalance(0l)
                .withAccountType(AccountType.Savings);

        Mockito.when(transactionService.withdraw(Mockito.any(Double.class), Mockito.any(Account.class)))
                .thenReturn(account.getBalance() - newTransaction.getAmount());

        Mockito.when(accountRepository.findByAccountNumber(Mockito.anyLong()))
                .thenReturn(account);

        mvc.perform(MockMvcRequestBuilders
                .put("/users/accounts/withdraw/{accountNumber}/{amount}", 78l, 20.0)
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_XML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(account.getUserId()));
    }

    @Test
    public void accountBalanceExceptionTest() throws Exception {

        Transaction newTransaction = new Transaction();

        //List<Account> list = new ArrayList<>();
        Account account = new Account()
                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
                .withUserId("12")
                .withAccountNumber(78l)
                .withBalance(30)
                .withAccountType(AccountType.Savings);

        Mockito.when(accountRepository.findByAccountNumber(Mockito.anyLong()))
                .thenReturn(account);

        Mockito.when(transactionService.withdraw(Mockito.any(Double.class), Mockito.any(Account.class)))
                .thenThrow(LowBalanceException.class);

        mvc.perform(MockMvcRequestBuilders
                .put("/users/accounts/withdraw/{accountNumber}/{amount}", 78l, 20.0)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnavailableForLegalReasons())
                .andExpect(status().reason(equalTo("Account balance is too low.")));
    }

    @Test
    public void reportTest() throws Exception {

       // List<Account> list = new ArrayList<>();
//        Account account = new Account()
//                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
//                .withUserId("12")
//                .withAccountNumber(78l)
//                .withBalance(0l)
//                .withAccountType(AccountType.Checking);
        List<Transaction> transactionList=new ArrayList<>();
        Mockito.when(transactionService.report(Mockito.any(String.class), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
                .thenReturn(transactionList);

        LocalDate startDate = LocalDate.now().minusDays(1l);
        LocalDate endDate = LocalDate.now().plusDays(1l);
        mvc.perform(MockMvcRequestBuilders
                .put("/users/accounts/report/{accountId}/{startDate}/{endDate}", "0e4c1211-2c58-4956-b523-ed0d64dc54c4", startDate, endDate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

    }

    //report using XML data
    @Test
    public void reportXmlTest() throws Exception {

       // List<Account> list = new ArrayList<>();
//        Account account = new Account()
//                .withId("0e4c1211-2c58-4956-b523-ed0d64dc54c4")
//                .withUserId("12")
//                .withAccountNumber(78l)
//                .withBalance(0l)
//                .withAccountType(AccountType.Checking);
        List<Transaction> transactionList;
        Mockito.when(transactionList = transactionService.report(Mockito.any(String.class), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
                .thenReturn(transactionList);

        LocalDate startDate = LocalDate.now().minusDays(1l);
        LocalDate endDate = LocalDate.now().plusDays(1l);
        mvc.perform(MockMvcRequestBuilders
                .put("/users/accounts/reportXML/{accountNumber}/{startDate}/{endDate}", 78l, startDate, endDate)
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_XML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("<List/>"));

    }

    @Test
    public void toStringForReportTest() {

        Transaction transaction1 = new Transaction(), transaction2 = new Transaction();

        transaction1.setId(1);
        transaction1.setTransactionDate(LocalDateTime.parse("2019-04-04T17:30:49.189"));
        transaction1.setType(TransactionType.Credit);
        transaction1.setStatus(TransactionStatus.In_Progress);
        transaction1.setAmount(108.0);

        transaction2.setId(2);
        transaction2.setTransactionDate(LocalDateTime.parse("2019-04-04T17:33:26.158"));
        transaction2.setType(TransactionType.Debit);
        transaction2.setStatus(TransactionStatus.In_Progress);
        transaction2.setAmount(10.0);

        String expected = "[{\"ID\":\"1\",\"TransactionDate\":\"2019-04-04T17:30:49.189\"," +
                "\"TransactionType\":\"Credit\",\"TransactionStatus\":\"In_Progress\",\"" +
                "TransactionAmount\":\"108.0\"},{\"ID\":\"2\",\"TransactionDate\":\"2019-0" +
                "4-04T17:33:26.158\",\"TransactionType\":\"Debit\",\"TransactionStatus\":\"" +
                "In_Progress\",\"TransactionAmount\":\"10.0\"}]";
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);

        Assert.assertEquals("Representation Should Matched", expected, new TransactionController().toStringForReport(transactions));

    }

    //XML transformer
    @Test
    public void toStringForReportXMLTest() {

        Transaction transaction1 = new Transaction(), transaction2 = new Transaction();

        transaction1.setId(1);
        transaction1.setTransactionDate(LocalDateTime.parse("2019-04-04T17:30:49.189"));
        transaction1.setType(TransactionType.Credit);
        transaction1.setStatus(TransactionStatus.In_Progress);
        transaction1.setAmount(108.0);

        transaction2.setId(2);
        transaction2.setTransactionDate(LocalDateTime.parse("2019-04-04T17:33:26.158"));
        transaction2.setType(TransactionType.Debit);
        transaction2.setStatus(TransactionStatus.In_Progress);
        transaction2.setAmount(10.0);

        String expected = "<List><ID>1</ID><TransactionDate>2019-04-04T17:30:49.189</TransactionDate>" +
                "<TransactionStatus>In_Progress</TransactionStatus><TransactionAmount>108.0</TransactionAmount>" +
                "<ID>2</ID><TransactionDate>2019-04-04T17:33:26.158</TransactionDate>" +
                "<TransactionStatus>In_Progress</TransactionStatus><TransactionAmount>10.0</TransactionAmount></List>";
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);

        Assert.assertEquals("Representation Should Matched", expected, new TransactionController().toStringForReportXML(transactions));

    }

}
