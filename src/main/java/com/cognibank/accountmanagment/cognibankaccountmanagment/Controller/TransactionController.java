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
@RequestMapping("/api/v1/accounts/")
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

    @PutMapping("report/{accountNumber}/{startDate}/{endDate}")
    public String report(@PathVariable long accountNumber, @PathVariable String startDate, @PathVariable String endDate) {
        return toStringForReport(transactionService.report(accountNumber, LocalDate.parse(startDate), LocalDate.parse(endDate)));
    }
    //xml report

    @PutMapping(value={"reportXML/{accountNumber}/{startDate}/{endDate}"},
            produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public List<Transaction> reportXML(@PathVariable long accountNumber, @PathVariable String startDate, @PathVariable String endDate, HttpServletResponse response) {
        response.setHeader("Content type ","xml");
        return transactionService.report(accountNumber, LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    //Json imitation
    public String toStringForReport(List<Transaction> transactionList) {
        final StringBuilder result = new StringBuilder();
        transactionList.stream().forEach(tran -> {
            result.append(
                    "{\"ID\":\"" + tran.getId() + "\"," +
                            "\"Transaction Date\":\"" + tran.getTransactionDate() + "\"," +
                            "\"Transaction Type\":\"" + tran.getType() + "\"," +
                            "\"Transaction Status:\"" + tran.getStatus() + "\"," +
                            "\"Transaction Amount:\"" + tran.getAmount() + "\"},");
        });
        if (result.length() != 0)
            return "{" + result.replace(result.length() - 1, result.length(), "}");
        else
            return "{}";
    }

    public String toStringForReportXML(List<Transaction> transactionList) {
        final StringBuilder result = new StringBuilder();
        transactionList.stream().forEach(tran -> {
            result.append(
                    "<ID>"+tran.getId()+"</ID>"
                    );
        });
        return "<report>"+result.toString()+"</report>";

    }


}
