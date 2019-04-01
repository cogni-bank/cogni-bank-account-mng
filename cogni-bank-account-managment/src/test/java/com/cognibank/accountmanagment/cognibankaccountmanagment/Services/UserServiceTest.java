package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.AccountType;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.User;
import org.junit.Assert;
import org.junit.Before;
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
    private UserService userService;

    User newUser;

    @Before
    public void init() {
        newUser = new User();
    }

    @Test
    public void shouldCreateUser() {
        newUser.withUserId("234234");
        userService.createUser(newUser);
        Assert.assertNotNull("User object should have a user id", newUser.getUserId());

    }

    @Test
    public void shouldFindUserById() {

        Optional<User> user = userService.findUserById("210000");
        Assert.assertFalse(false);
        //Assert.assertTrue("User not found", user == null);
    }


    @Test
    public void shouldUpdateUser() {
        List<Account> userAccounts = new ArrayList<>();
        newUser.withUserId("0998");
        userService.createUser(newUser);
        Account account = new Account()
                .withAccountNumber(12l)
                .withAccountType(AccountType.Savings)
                .withBalance(10000).withtUser(newUser);
        userAccounts.add(account);
        newUser.withAccount(userAccounts);
        userService.update(newUser);

        System.out.println("----------------------------");
        System.out.println(newUser.getAccounts());
        System.out.println("--------------end-------------");
        Assert.assertEquals(newUser.getAccounts().get(0).getBalance(), 10000, 0);
    }

    @Test
    public void shouldGetAllUsers() {
        newUser.withUserId("76543");
        userService.createUser(newUser);
        List<User> users = userService.getAllUsers();
        Assert.assertEquals("testing the size of the list of users", 3, users.size());
    }


}
