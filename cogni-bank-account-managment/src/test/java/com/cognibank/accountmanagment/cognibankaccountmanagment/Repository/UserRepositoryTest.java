package com.cognibank.accountmanagment.cognibankaccountmanagment.Repository;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.AccountType;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.User;
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
public class UserRepositoryTest {


    @Autowired
    UserRepository userRepository;

    @Test
    public void userCreateTest() {
        User user = new User();
        user.withUserId("234234");
        List<Account> userAccounts = new ArrayList<>();


        userAccounts.add( new Account()
                .withBalance(0l)
                .withAccountNumber(123)
                .withAccountType(AccountType.Checking)
                .withtUser(user));
        user.withAccount(userAccounts);
       user = userRepository.save(user);

      Assert.assertNotNull("Get registered users id", user.getUserId());
     }
}
