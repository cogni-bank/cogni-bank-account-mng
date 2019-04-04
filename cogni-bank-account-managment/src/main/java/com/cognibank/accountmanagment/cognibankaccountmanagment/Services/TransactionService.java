package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions.AccountNotFoundException;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.*;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionRepository transactionRepository;


    public List<Transaction> getTransactionsByCustomerId(String id) {
        return transactionRepository.findTransactionsByCustomerId(id);
    }

    public List<Transaction> getTransactionByAccountNumber(long accountNumber) {

        return transactionRepository.findTransactionsByAccountNumber(accountNumber);
    }

    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    //Do we need this?
    public void deleteTransaction(String id) {
        transactionRepository.deleteById(id);
    }

    //Do we need this?
    public void updateTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public double deposit(double amount, Account account) {
        if (account.getStatus().equals("ACTIVE")) {
            Transaction transaction = new Transaction()
                    .withAccount(account)
                    .withCustomerId(account.getUserId())
                    .setAmount(amount)
                    .withType(TransactionType.Credit)
                    .withStatus(TransactionStatus.In_Progress);
            transactionRepository.save(transaction);
            Double currentBalance = account.getBalance() + transaction.getAmount();
            account.withBalance(currentBalance);
            return currentBalance;
        } else {
            //May be we should throw an exception here if the account is not active
            return 0;

        }
    }

    public List<Transaction> report(long accountNumber, LocalDate startDate, LocalDate endDate) {
        final Account account = accountService.getAccountByAccountNumber(accountNumber);

        List<Transaction> allTransaction = transactionRepository.findByAccountId(account.getId())
                .orElseThrow(AccountNotFoundException::new)
                .stream()
                .filter(dateReport -> (dateReport.getTransactionDate().isBefore(manageDate(endDate)) && dateReport.getTransactionDate().isAfter(manageDate(startDate))))
                .collect(Collectors.toList());
        return allTransaction;
    }

    public LocalDateTime manageDate(LocalDate localDate){
        return localDate.atStartOfDay();
    }



}
