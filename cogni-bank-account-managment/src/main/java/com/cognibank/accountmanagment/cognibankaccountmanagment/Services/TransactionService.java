package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.*;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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


}
