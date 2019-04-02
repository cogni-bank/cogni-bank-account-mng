package com.cognibank.accountmanagment.cognibankaccountmanagment.Controller;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.AccountType;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.User;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.AccountService;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Account create(@PathVariable String id, @PathVariable AccountType accountType) {
        User user = userService.findUserById(id);

        final Account newAccount = new Account().withUser(user)
                .withAccountType(accountType)
                .withAccountNumber(0L)
                .withBalance(0L);

        Account account = accountService.createAccount(newAccount, user);
        return account;
    }

}
