package com.cognibank.accountmanagment.cognibankaccountmanagment.Controller;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions.LowBalanceException;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Transaction;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.AccountRepository;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.TransactionService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Api(tags = {"Transactions API"})
@SwaggerDefinition(
        tags = {
                @Tag(name = "Transaction API", description = "Transactions API ,deals with the user transactions ")
        }
)

@RestController
@RequestMapping("/users/accounts/")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountRepository accountRepository;

    @ApiOperation(value = "Deposits amount ", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful. Returns user Id"),
            @ApiResponse(code = 400, message = "Badly formed request, or validations failed.")
    })
    @PutMapping("deposit/{accountNumber}/{amount}")
    public String deposit(@PathVariable long accountNumber, @PathVariable double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        transactionService.deposit(amount, account);
        return account.getUserId();
    }

    @ApiOperation(value = "Withdrawal amount ", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful. Returns user Id"),
            @ApiResponse(code = 400, message = "Badly formed request, or validations failed.")
    })
    @PutMapping("withdraw/{accountNumber}/{amount}")
    public String withdraw(@PathVariable long accountNumber, @PathVariable double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);

        transactionService.withdraw(amount, account);
        return account.getUserId();
    }

    @ApiOperation(value = "Fetches account number ", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful. Returns user's account number"),
            @ApiResponse(code = 400, message = "Badly formed request, or validations failed.")
    })
    @PutMapping("getAccountNumberById/{accountId}")
    public String getAccountNumberById(@PathVariable  String accountId){
        return accountRepository.findById(accountId).get().getAccountNumber()+"";
    }

    @ApiOperation(value = "Transaction Report ", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful. Returns Transactions"),
            @ApiResponse(code = 400, message = "Badly formed request, or validations failed.")
    })
    //@PutMapping("report/{accountNumber}/{startDate}/{endDate}")
    @PutMapping(value = {"report/{accountId}/{startDate}/{endDate}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String report(@PathVariable String accountId, @PathVariable String startDate, @PathVariable String endDate) {
        List<Transaction> transactions=transactionService.report(accountId, LocalDate.parse(startDate), LocalDate.parse(endDate));
        return toStringForReport(transactions);
    }

    @ApiOperation(value = "Funds Transfer ", response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful. Returns Transaction Id"),
            @ApiResponse(code = 400, message = "Badly formed request, or validations failed.")
    })
    @PutMapping("transfer/{originAccountNumber}/{destinationAccountNumber}/{amount}")
    public long  transfer(@PathVariable long originAccountNumber, @PathVariable long destinationAccountNumber, @PathVariable double amount) {
        //will return the transaction id
        return transactionService.transfer(originAccountNumber,destinationAccountNumber,amount);
    }

    @ApiOperation(value = "Transaction Report in XML format", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful. Returns Transactions"),
            @ApiResponse(code = 400, message = "Badly formed request, or validations failed.")
    })
    @PutMapping(value={"reportXML/{accountId}/{startDate}/{endDate}"},
            produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public String reportXML(@PathVariable String accountId, @PathVariable String startDate, @PathVariable String endDate, HttpServletResponse response) {
        response.setHeader("Content type ","xml");
        List<Transaction> transactionList = transactionService.report(accountId, LocalDate.parse(startDate), LocalDate.parse(endDate));
        return toStringForReportXML(transactionList);
    }

    //Json imitation
    public String toStringForReport(List<Transaction> transactionList) {
        final StringBuilder result = new StringBuilder();
        if(transactionList==null ) return "[]";
        if(transactionList.isEmpty()) return "[]";
            transactionList.stream().forEach(tran -> {
            result.append(
                    "{\"ID\":\"" + tran.getId() + "\"," +
                            "\"TransactionDate\":\"" + tran.getTransactionDate() + "\"," +
                            "\"TransactionType\":\"" + tran.getType() + "\"," +
                            "\"TransactionStatus\":\"" + tran.getStatus() + "\"," +
                            "\"TransactionAmount\":\"" + tran.getAmount() + "\"},");
        });

            return "[" + result.replace(result.length() - 1, result.length(), "]");


    }

    // xml report transformer
    public String toStringForReportXML(List<Transaction> transactionList) {
        final StringBuilder result = new StringBuilder();
        transactionList.stream().forEach(tran -> {
            result.append(
                    "<ID>"+tran.getId()+"</ID>"
                    +"<TransactionDate>"+tran.getTransactionDate()+"</TransactionDate>"
                            +"<TransactionStatus>"+tran.getStatus().name()+"</TransactionStatus>"
                            +"<TransactionAmount>"+tran.getAmount()+"</TransactionAmount>"
                    );
        });
        return "<List>"+result.toString()+"</List>";

    }


}
