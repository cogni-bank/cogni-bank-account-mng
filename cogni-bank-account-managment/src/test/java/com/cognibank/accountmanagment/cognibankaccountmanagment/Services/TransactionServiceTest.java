package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;


import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.*;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.TransactionRepository;
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
    private UserService userService;

    User newUser;

    @Before
    public void init(){
        newUser = new User();

    }
    @Test
    public void shouldReturnCustomerIdGivenASpecificTransaction(){
        List<Account> userAccounts;
        Transaction transaction = new Transaction();

        userAccounts = new ArrayList<>();
        newUser.withUserId("0998");
        userService.createUser(newUser);
        Account account = new Account()
                .withAccountNumber(12l)
                .withAccountType(AccountType.Savings)
                .withBalance(10000).withtUser(newUser);
        userAccounts.add(account);
        newUser.withAccount(userAccounts);
        userService.createUser(newUser);

        transaction
                .withAccount(newUser.getAccounts().get(0))
                .setAmount(100)
                .withDestinationAccount("Savings")
                .withCustomerId(newUser.getUserId())
                .withType(TransactionType.Debit);

        transactionService.createTransaction(transaction);
        List<Transaction> tran = transactionService.getTransactionsByCustomerId(newUser.getUserId());
        Assert.assertEquals("User tied to the transaction should have the same id as actual",tran.get(0).getCustomerId() , "0998" );
    }

    @Test
    public void shouldReturnCustemerTransactionForAGivenAccountNumber(){

        List<Account> userAccounts;
        Transaction transactionByAccountNumber = new Transaction();

        userAccounts = new ArrayList<>();
        newUser.withUserId("001");
        userService.createUser(newUser);
        Account account = new Account()
                .withAccountNumber(6l)
                .withAccountType(AccountType.Savings)
                .withBalance(98000).withtUser(newUser);
        userAccounts.add(account);
        newUser.withAccount(userAccounts);
        userService.createUser(newUser);

        transactionByAccountNumber
                .withAccount(newUser.getAccounts().get(0))
                .setAmount(800)
                .withDestinationAccount("Savings")
                .withCustomerId(newUser.getUserId())
                .withType(TransactionType.Debit);
        transactionService.createTransaction(transactionByAccountNumber);

        List<Transaction> tranByAccNum = transactionService.getTrasactionByAccountNumber(6);

        Assert.assertEquals(tranByAccNum.get(0).getAccount().getAccountNumber(), 6);




    }
}
