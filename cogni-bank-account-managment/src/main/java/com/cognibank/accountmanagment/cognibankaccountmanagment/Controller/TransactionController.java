package com.cognibank.accountmanagment.cognibankaccountmanagment.Controller;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.AccountRepository;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts/")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountRepository accountRepository;

    @PutMapping("deposit/{accountNumber}/{amount}")
    public String deposit(@PathVariable long accountNumber, @PathVariable double amount) {
        Account account= accountRepository.findByAccountNumber(accountNumber);
        transactionService.deposit(amount,account);
        String userId = account.getUser().getUserId();
        return userId;

    }


}
