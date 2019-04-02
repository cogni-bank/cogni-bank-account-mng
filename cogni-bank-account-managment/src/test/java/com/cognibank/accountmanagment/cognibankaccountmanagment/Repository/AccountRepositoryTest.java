package com.cognibank.accountmanagment.cognibankaccountmanagment.Repository;


import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.AccountType;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void shouldGetAnAccountWithItsAccountNumber() {
        User user = new User()
                .withUserId("a123414123123");
        user = userRepository.save(user);


        long expectedAccountNumber = 123L;
        Account newAccount = new Account()
                .withAccountNumber(expectedAccountNumber)
                .withAccountType(AccountType.Checking)
                .withBalance(100L)
                .withUser(user);
        newAccount = accountRepository.save(newAccount);

        Account foundAccount = accountRepository.findByAccountNumber(expectedAccountNumber);

        assertEquals("Account should be the same", newAccount, foundAccount);

    }
}
