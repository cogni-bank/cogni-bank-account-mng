package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions.AccountNotFoundException;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        accountRepository.save(account);
        return account;
    }

    public List<Account> getUserAccounts(String userId) {
        return
                accountRepository.findByUserId(userId)
                        .orElseThrow(AccountNotFoundException::new);
    }

    public long numberOfAccount() {
        return accountRepository.count();
    }


}
