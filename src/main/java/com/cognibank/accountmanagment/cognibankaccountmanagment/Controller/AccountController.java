package com.cognibank.accountmanagment.cognibankaccountmanagment.Controller;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.AccountType;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.AccountsList;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.AccountService;
import io.swagger.annotations.*;
import org.json.JSONObject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
@Api(tags = {"Accounts API"})
@SwaggerDefinition(
        tags = {
                @Tag(name = "Accounts API", description = "Accounts API ,deals with the user account details")
        }
)
@RestController
@RequestMapping("/users/accounts/")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Creating an account for a user.
     *
     * @param accountType Account type
     * @param userId          User ID
     * @return returning account id to the client ok ok
     */
    @ApiOperation(value = "Creates an account", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful. Returns Account ID"),
            @ApiResponse(code = 400, message = "Badly formed request, or validations failed.")
    })
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

    @ApiOperation(value = "Given an userId returns accounts list associated with particular user", response = AccountsList.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful. Returns Accounts list associated with particular user"),
            @ApiResponse(code = 400, message = "Badly formed request, or validations failed.")
    })
    @PutMapping("/accountsList/{userId}")
    public AccountsList getAccountsList(@PathVariable String userId) {
        //will return the transaction id
        AccountsList accountsList = new AccountsList();
         accountsList.setAccounts(accountService.getUserAccounts(userId));
         return accountsList;
    }


    @ApiOperation(value = "Given an accountNumber updates the status of the account ", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful. Returns Accounts Status"),
            @ApiResponse(code = 400, message = "Badly formed request, or validations failed.")
    })

    @PutMapping("/update/{accountNumber}/{status}")

    public String updateStatus( @PathVariable Long accountNumber, @PathVariable String status) {

       return accountService.changeAccountStatus(accountNumber, status);

    }

    // just a test for RabbitMQ to be deleted
    //localhost:9300/users/accounts/rabbitmq/send/3400000023/kanasounna@gmail.com/kana/100
    @ApiOperation(value = "Send Message -RabbitMQ ", response = String.class)
    @GetMapping("/rabbitmq/send/{accountNumber}/{email}/{userName}/{amount}")
    public String sendMessage(@PathVariable long accountNumber, @PathVariable String email, @PathVariable String userName, @PathVariable double amount){
        String objectToSend="{\"accountNumber\":\""
                +accountNumber+"\",\"email\":\""+email+"\",\"userName\":\""+userName+"\",\"amount\":\""+amount+"\"}";
        //System.out.println(""objectToSend);
        JSONObject jsonObj = new JSONObject(objectToSend);
        rabbitTemplate.convertAndSend("LOWERBALANCE_EXCHANGE","LOWBALANCE_KEY",jsonObj.toString());
        return jsonObj.toString();
    }
    //localhost:9300/users/accounts/rabbitmq/send/3400000023/kanasounnaw@gmail.com/kana/100
}
