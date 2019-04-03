package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;


import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
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
}
