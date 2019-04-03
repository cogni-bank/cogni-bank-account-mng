package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.*;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

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

    public void deleteTransaction(String id) {
        transactionRepository.deleteById(id);
    }

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
        }
        else{
            return 0;

        }
    }
    public List<Transaction> report(long accountNumber, LocalDateTime startDate, LocalDateTime endDate){

       List<Transaction> allTransaction = transactionRepository.findAll()
                                            .stream().filter(acnb -> /*acnb.getAccount().getAccountNumber()*/78l==accountNumber)
                                                    .filter(dateReport -> (dateReport.getTransactionDate().isBefore(endDate) && dateReport.getTransactionDate().isAfter(startDate)))
                                                    .collect(Collectors.toList());
             return allTransaction;
    }


}
