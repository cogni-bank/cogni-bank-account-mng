package com.cognibank.accountmanagment.cognibankaccountmanagment.Controller;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions.LowBalanceException;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Transaction;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.AccountRepository;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/users/accounts/")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountRepository accountRepository;

    @PutMapping("deposit/{accountNumber}/{amount}")
    public String deposit(@PathVariable long accountNumber, @PathVariable double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        transactionService.deposit(amount, account);
        return account.getUserId();
    }

    @PutMapping("withdraw/{accountNumber}/{amount}")
    public String withdraw(@PathVariable long accountNumber, @PathVariable double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);

        transactionService.withdraw(amount, account);
        return account.getUserId();
    }
    @PutMapping("getAccountNumberById/{accountId}")
    String getAccountNumberById(@PathVariable  String accountId){
        return accountRepository.findById(accountId).get().getAccountNumber()+"";
    }
    //@PutMapping("report/{accountNumber}/{startDate}/{endDate}")
    @PutMapping(value = {"report/{accountId}/{startDate}/{endDate}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String report(@PathVariable String accountId, @PathVariable String startDate, @PathVariable String endDate) {
        //long accountNumber=getAccountNumberById(accountId);
       // System.out.println(accountNumber +" inside report");
        List<Transaction> transactions=transactionService.report(accountId, LocalDate.parse(startDate), LocalDate.parse(endDate));
        return toStringForReport(transactions);
    }
//    @PutMapping("report/{accountId}/{startDate}/{endDate}")
//    public List<Transaction> report(@PathVariable long accountNumber, @PathVariable String startDate, @PathVariable String endDate) {
//        return transactionService.report(accountNumber, LocalDate.parse(startDate), LocalDate.parse(endDate));
//    }
    //xml report
    @PutMapping(value={"reportXML/{accountId}/{startDate}/{endDate}"},
            produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public List<Transaction> reportXML(@PathVariable String accountId, @PathVariable String startDate, @PathVariable String endDate, HttpServletResponse response) {
        response.setHeader("Content type ","xml");
        return transactionService.report(accountId, LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    //Json imitation
    public String toStringForReport(List<Transaction> transactionList) {
        final StringBuilder result = new StringBuilder();
        if(transactionList==null) return "[]";
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
                    );
        });
        return "<List>"+result.toString()+"</List>";

    }


}
