package com.cognibank.accountmanagment.cognibankaccountmanagment.Controller;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.AccountType;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.User;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.AccountService;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/v1/accounts/")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    /**
     * Creating an account for a user.
     *
     * @param accountType Account type
     * @param id          User ID
     * @return returning account id to the client
     */
    @PutMapping("create/{id}/{accountType}")
    public String create(@PathVariable String id, @PathVariable AccountType accountType) {
        Random random =  new Random();
        int lastTwoDigits = random.nextInt(100);
        User user = userService.findUserById(id);
        Long accountNumber=Long.parseLong((accountService.numberOfAccount()+34000001)+""+lastTwoDigits);
        final Account newAccount = new Account().withUser(user)
                .withAccountType(accountType)
                .withAccountNumber(accountNumber)
                .withBalance(0L);

        Account account = accountService.createAccount(newAccount, user);
        return account.getId();
    }

}
