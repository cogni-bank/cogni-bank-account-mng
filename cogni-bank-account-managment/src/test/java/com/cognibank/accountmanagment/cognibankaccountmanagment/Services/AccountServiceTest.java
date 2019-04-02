package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;


import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.AccountType;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.User;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void createAccountTest(){
        List<Account> listAccounts = new ArrayList<>();
           User user = new User().withUserId("12").withAccount(listAccounts);
            userRepository.save(user);
        long currentNumberOfAccounts  = user.getAccounts().size();

        Account account = new Account()
                   .withBalance(0l).
                        withAccountNumber(78l)
                         .withId(user.getUserId())
                         .withAccountType(AccountType.Checking).withUser(user);
//User user , String accountType
        listAccounts.add(account);
         user.withAccount(listAccounts);
        accountService.createAccount(account,user);
        Assert.assertEquals("Number of account should be + 1",currentNumberOfAccounts+1,user.getAccounts().size());
        Assert.assertTrue("Whether the accountType matches",account.getAccountType().compareTo(AccountType.Savings)!=0);



    }

}
