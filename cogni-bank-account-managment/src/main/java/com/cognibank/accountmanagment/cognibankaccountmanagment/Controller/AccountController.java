package com.cognibank.accountmanagment.cognibankaccountmanagment.Controller;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.AccountType;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/v1/accounts/")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * Creating an account for a user.
     *
     * @param accountType Account type
     * @param userId          User ID
     * @return returning account id to the client
     */
    @PutMapping("create/{userId}/{accountType}")
    public String create(@PathVariable String userId, @PathVariable AccountType accountType) {


        final Account newAccount = new Account()
                .withUserId(userId)
                .withAccountType(accountType)
                .withAccountNumber(this.generateAccountNumber())
                .withBalance(0L);

        Account account = accountService.createAccount(newAccount);
        return account.getId();
    }
    private long generateAccountNumber(){
        Random random =  new Random();
        int lastTwoDigits = random.nextInt(100);
        return Long.parseLong((accountService.numberOfAccount()+34000001)+""+((lastTwoDigits<10)?(0+""+lastTwoDigits):lastTwoDigits));

    }


    @PutMapping("update/{accountNumber}/{status}")

    public String updateStatus( @PathVariable Long accountNumber, @PathVariable String status) {

       return accountService.changeAccountStatus(accountNumber, status);

    }

}
