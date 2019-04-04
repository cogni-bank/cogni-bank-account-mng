package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;


import com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions.LowBalanceException;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;


    @Before
    public void init() {

    }

    @Test
    public void shouldReturnCustomerIdGivenASpecificTransaction() {
        //List<Account> listAccounts = new ArrayList<>();

        Account account = new Account().withBalance(0L).withAccountNumber(78L).withAccountType(AccountType.Checking).withUserId("222");

        //listAccounts.add(account);
        accountService.createAccount(account);

        Transaction transaction = new Transaction()
                .withAccount(account)
                .setAmount(100)
                .withDestinationAccount("Checking")
                .withCustomerId(account.getUserId())
                .withType(TransactionType.Debit);

        transactionService.createTransaction(transaction);

        List<Transaction> tran = transactionService.getTransactionsByCustomerId(account.getUserId());

        Assert.assertEquals("User tied to the transaction should have the same id as actual",
                tran.get(0).getCustomerId(), "222");
    }

    @Test
    public void shouldReturnCustomerTransactionForAGivenAccountNumber() {

        List<Account> userAccounts = new ArrayList<>();

        Transaction transaction = new Transaction();

        Account account = new Account()
                .withAccountNumber(6l)
                .withAccountType(AccountType.Checking)
                .withBalance(98000).withUserId("222");
        userAccounts.add(account);
        accountService.createAccount(account);
        transaction
                .withAccount(account)
                .setAmount(800)
                .withDestinationAccount("Checking")
                .withCustomerId("222")
                .withType(TransactionType.Debit);

        transactionService.createTransaction(transaction);
        transactionService.getTransactionByAccountNumber(6l);

        Assert.assertEquals(transaction.getAccount().getAccountNumber(), 6);

    }

    @Test
    @Transactional
    public void depositTest(){
        Account account = new Account()
                .withUserId("1")
                .withAccountType(AccountType.Savings)
                .withAccountNumber(10l)
                .withBalance(100.0);
        account = accountService.createAccount(account);
        double actualBalance = transactionService.deposit(10.0, account);
        assertEquals("Account balance should be updated after the deposit.", 110.0,actualBalance, 0.1);
    }


    @Test
    @Transactional
    public void withdrawTest() throws Exception{
        Account account = new Account()
                .withUserId("1")
                .withAccountType(AccountType.Savings)
                .withAccountNumber(10l)
                .withBalance(100.0);
        account = accountService.createAccount(account);
        double actualBalance = transactionService.withdraw(10.0, account);
        assertEquals("Account balance should be updated after the deposit.", 90.0,actualBalance, 0.1);
    }


    @Test//(expected = LowBalanceException.class)
    @Transactional
    public void withdrawTestForLowBalance() throws Exception{
        Account account = new Account()
                .withUserId("1")
                .withAccountType(AccountType.Savings)
                .withAccountNumber(10l)
                .withBalance(100.0);
        account = accountService.createAccount(account);
        double actualBalance = transactionService.withdraw(80.0, account);
    }

    @Test
    @Transactional
    public void reportTest() {
//        final String userId = "ABCD1234";

        Account account = new Account()
                .withBalance(0L)
                .withAccountNumber(78L)
                .withUserId("ABCD1234")
                .withAccountType(AccountType.Checking);
        account = accountService.createAccount(account);
        // Transaction transaction=new Transaction();
        LocalDate startDate=LocalDate.now().minusDays(1l);
        LocalDate endDate=LocalDate.now().plusDays(1l);
        transactionService.deposit(57676,account);
        transactionService.deposit(157676,account);
        transactionService.deposit(3,account);

        transactionService.deposit(56,account);
        transactionService.deposit(56,account);

        List<Transaction> transactions=transactionService.report(78L,startDate, endDate);
        Assert.assertEquals("Number of transaction should be", 5, transactions.size());
        assertEquals("Account must be the same", account, transactions.get(0).getAccount());
    }
}
