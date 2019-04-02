package com.cognibank.accountmanagment.cognibankaccountmanagment.Controller;

import com.cognibank.accountmanagment.cognibankaccountmanagment.DTO.AccountDTO;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions.AccountNotFoundException;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions.UserAccountNotFoundException;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Transaction;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.User;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.AccountService;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.TransactionService;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @PutMapping("/")
    public void create(@Valid Account account) throws  UserAccountNotFoundException {


        throw new UserAccountNotFoundException("Customer was not found while adding account");
    }

}
