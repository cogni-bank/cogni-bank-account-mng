package com.cognibank.accountmanagment.cognibankaccountmanagment.Repository;


import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.AccountType;
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
    private AccountRepository accountRepository;

    @Test
    public void shouldGetAnAccountWithItsAccountNumber() {

        long expectedAccountNumber = 123L;
        Account newAccount = new Account()
                .withAccountNumber(expectedAccountNumber)
                .withAccountType(AccountType.Checking)
                .withBalance(100L)
                .withUserId("a123414123123");
        newAccount = accountRepository.save(newAccount);

        Account foundAccount = accountRepository.findByAccountNumber(expectedAccountNumber);

        assertEquals("Account should be the same", newAccount, foundAccount);

    }
}
