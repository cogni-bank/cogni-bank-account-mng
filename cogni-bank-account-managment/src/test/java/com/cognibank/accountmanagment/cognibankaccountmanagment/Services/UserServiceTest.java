package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.AccountType;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.font.TrueTypeFont;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void createUserTest(){
        User newUser = new User();
        newUser.withUserId("234234");
        userService.createUser(newUser);
        Assert.assertNotNull("User object should have a user id", newUser.getUserId());
    }

    @Test
    public void findUserById(){
        Assert.assertTrue(true);
    }

    @Test
    public void getAllUsersTest(){
        User newUser = new User();
        newUser.withUserId("23");
        userService.createUser(newUser);

       List<User> users = userService.getAllUsers();
       Assert.assertEquals("testing the size of the list of users",1,users.size());
    }

    @Test
    public void updateUser(){
        List<Account> userAccounts = new ArrayList<>();
        User newUser = new User();
        newUser.withUserId("0998");
        userService.createUser(newUser);
        Account account = new Account().withAccountNumber(12l)
                .withAccountType(AccountType.Savings)
                .withBalance(10000).withtUser(newUser);
        userAccounts.add(account);
        newUser.withAccount(userAccounts);
        Assert.assertEquals( account.getBalance() ,10000);

    }


}
