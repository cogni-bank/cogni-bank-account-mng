package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions.AccountNotFoundException;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions.LowBalanceException;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.*;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.AccountRepository;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.TransactionRepository;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

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
            account.setBalance(currentBalance);
            accountRepository.save(account);
            return currentBalance;
        } else {
            //May be we should throw an exception here if the account is not active
            return 0;

        }
    }

    public void sendMessage(long accountNumber, String email, String userName, double amount){
        String objectToSend="{\"accountNumber\":\""
                +accountNumber+"\",\"email\":\""+email+"\",\"userName\":\""+userName+"\",\"amount\":\""+amount+"\"}";
        //System.out.println(""objectToSend);
        JSONObject jsonObj = new JSONObject(objectToSend);
        rabbitTemplate.convertAndSend("LOWERBALANCE_EXCHANGE","LOWBALANCE_KEY",jsonObj.toString());
    }
    public double withdraw(double amount, Account account) throws LowBalanceException{
        if (account.getStatus().equals("ACTIVE")) {
            Transaction transaction = new Transaction()
                    .withAccount(account)
                    .withCustomerId(account.getUserId())
                    .setAmount(amount)
                    .withType(TransactionType.Debit)
                    .withStatus(TransactionStatus.In_Progress);
            transactionRepository.save(transaction);
            Double currentBalance = account.getBalance() - transaction.getAmount();
            account.withBalance(currentBalance);
            accountRepository.save(account);
            if( currentBalance < 25.0){

                /*RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Boolean> isUp = restTemplate
                    .getForEntity("http://container2:2002/apis/test",Boolean.class)
                 */
                //throw new LowBalanceException("Low balance: $"+currentBalance);
                sendMessage(account.getAccountNumber(),"kana@gmail.com","kana",account.getBalance());
                //send the message to the queue to notify the client by email or phone
            }
            return currentBalance;
        } else {
            //May be we should throw an exception here if the account is not active
            return 0;

        }
    }
////@Transactional
//    public List<Transaction> report(String accountId, LocalDate startDate, LocalDate endDate) {
//       // final Account account = accountService.getAccountByAccountNumber(accountNumber);
//
//        List<Transaction> allTransaction = transactionRepository.findByAccountId(accountId).get();
//        if(allTransaction!=null){
//            return allTransaction
//                .stream()
//                .filter(dateReport -> (dateReport.getTransactionDate().isBefore(manageDate(endDate)) && dateReport.getTransactionDate().isAfter(manageDate(startDate))))
//                .collect(Collectors.toList());
//
//        }
//
//        return null;
//    }

    //@Transactional
    public List<Transaction> report(String accountId, LocalDate startDate, LocalDate endDate) {
        // final Account account = accountService.getAccountByAccountNumber(accountNumber);

        System.out.println("accountId --> "+accountId);

        Optional<List<Transaction>> allTransaction = transactionRepository.findByAccountId(accountId);
        System.out.println("allTransaction --> "+allTransaction);

        if(allTransaction.isPresent()){
            return allTransaction.get()
                    .stream()
                    .filter(dateReport -> (dateReport.getTransactionDate().isBefore(manageDate(endDate)) && dateReport.getTransactionDate().isAfter(manageDate(startDate))))
                    .collect(Collectors.toList());

        }


        return null;
    }

    public LocalDateTime manageDate(LocalDate localDate){
        return localDate.atStartOfDay();
    }



}

