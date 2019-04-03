package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;


import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.AccountType;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Transaction;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.AccountRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @Test
    public void createAccountTest() {
        final String userId = "ABCD1234";

        Account account = new Account()
                .withBalance(0L)
                .withAccountNumber(78L)
                .withUserId("ABCD1234")
                .withAccountType(AccountType.Checking);
        account = accountService.createAccount(account);
        assertNotEquals("Account should receive a Unique ID", 0L, account.getId());

        List<Account> userAccounts = accountService.getUserAccounts(userId);

        assertEquals("Account should same", account, userAccounts.get(0));
    }
    @Test
    public void reportTest() {
//        final String userId = "ABCD1234";

        Account account = new Account()
                .withBalance(0L)
                .withAccountNumber(78L)
                .withUserId("ABCD1234")
                .withAccountType(AccountType.Checking);
        account = accountService.createAccount(account);
       // Transaction transaction=new Transaction();
        LocalDateTime startDate=LocalDateTime.now();
        LocalDateTime endDate=startDate.plusHours(1);
        transactionService.deposit(57676,account);
        transactionService.deposit(157676,account);
        transactionService.deposit(3,account);

        transactionService.deposit(56,account);
        transactionService.deposit(56,account);

        List<Transaction> transactions=transactionService.report(78L,startDate, endDate);
        Assert.assertEquals("Number of transaction should be", 5, transactions.size());
    }
}
