package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions.AccountNotActiveException;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions.LowBalanceException;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions.UserManagementServiceUnavailabeException;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Transaction;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.TransactionStatus;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.TransactionType;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.AccountRepository;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.TransactionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    @Autowired
    Environment env;

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
            try{
                throw new AccountNotActiveException();
            }catch(AccountNotActiveException e){
                e.printStackTrace();
            }
            return 0;

        }
    }

    public void sendMessage(long accountNumber, String email, String userName, double amount) {
        String objectToSend = "{\"accountNumber\":\""
                + accountNumber + "\",\"email\":\"" + email + "\",\"userName\":\"" + userName + "\",\"amount\":\"" + amount + "\"}";
        //System.out.println(""objectToSend);
        JSONObject jsonObj = new JSONObject(objectToSend);
        rabbitTemplate.convertAndSend(env.getProperty("spring.rabbitmq.api.directExchangeName"), env.getProperty("spring.rabbitmq.api.routingKey.lowbalance"), jsonObj.toString());
    }

    public void sendMessageOverDraft(long accountNumber, String email, String userName, double amount) {
        String objectToSend = "{\"accountNumber\":\""
                + accountNumber + "\",\"email\":\"" + email + "\",\"userName\":\"" + userName + "\",\"amount\":\"" + amount + "\"}";
        //System.out.println(""objectToSend);
        JSONObject jsonObj = new JSONObject(objectToSend);
        rabbitTemplate.convertAndSend(env.getProperty("spring.rabbitmq.api.directExchangeName"), env.getProperty("spring.rabbitmq.api.routingKey.overdraft"), jsonObj.toString());
    }

    public double withdraw(double amount, Account account) throws LowBalanceException {
        if (account.getStatus().equals("ACTIVE")) {
            Transaction transaction = new Transaction()
                    .withAccount(account)
                    .withCustomerId(account.getUserId())
                    .setAmount(amount)
                    .withType(TransactionType.Debit)
                    .withStatus(TransactionStatus.In_Progress);
            Double currentBalance = account.getBalance() - transaction.getAmount();
            if (currentBalance >= -100) {
                transactionRepository.save(transaction);
                account.withBalance(currentBalance);
                accountRepository.save(account);
            } else {
                String uri = env.getProperty("userManagement.getUserDetails");
                uri = uri + account.getUserId();
                System.out.println("uri for user management ----> " + uri);
                RestTemplate restTemplate = new RestTemplate();
                try {
                    ResponseEntity<String> newUserDetails = restTemplate.getForEntity(uri, String.class);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> map = objectMapper.readValue(newUserDetails.getBody(), new TypeReference<Map<String, Object>>() {
                    });

                    if (newUserDetails.getStatusCode().equals(HttpStatus.OK)) {
                        System.out.println("Message from user management ----> " + newUserDetails.getBody());
                        sendMessageOverDraft(account.getAccountNumber(), map.get("Email").toString(),
                                map.get("FirstName").toString(), account.getBalance());
                    }
                } catch (Exception e) {
                    throw new UserManagementServiceUnavailabeException();
                }
            }
            if (currentBalance < 25.0) {
                //send the message to the queue to notify the client by email or phone
                String uri = env.getProperty("userManagement.getUserDetails");
                uri = uri + account.getUserId();
                System.out.println("uri for user management ----> " + uri);
                RestTemplate restTemplate = new RestTemplate();
                try {
                    ResponseEntity<String> newUserDetails = restTemplate.getForEntity(uri, String.class);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> map = objectMapper.readValue(newUserDetails.getBody(), new TypeReference<Map<String, Object>>() {
                    });

                    if (newUserDetails.getStatusCode().equals(HttpStatus.OK)) {
                        System.out.println("Message from user management ----> " + newUserDetails.getBody());
                        sendMessage(account.getAccountNumber(), map.get("Email").toString(),
                                map.get("FirstName").toString(), account.getBalance());
                    }
                } catch (Exception e) {
                    throw new UserManagementServiceUnavailabeException();
                }

            }
            return currentBalance;
        } else {
            //May be we should throw an exception here if the account is not active
            try{
                throw new AccountNotActiveException();
            }catch(AccountNotActiveException e){
                e.printStackTrace();
            }
            return 0;

        }
    }

    @Transactional
    public long transfer(long originAccountNumber, long destinationAccountNumber, double amount) {
        Account originAccount = accountService.getAccountByAccountNumber(originAccountNumber);
        Account destinationAccount = accountService.getAccountByAccountNumber(destinationAccountNumber);
        if (originAccount.getStatus().equals("ACTIVE")) {
            Transaction transaction = new Transaction()
                    .withAccount(originAccount)
                    .withCustomerId(originAccount.getUserId())
                    .setAmount(amount)
                    .withType(TransactionType.Transfer)
                    .withStatus(TransactionStatus.In_Progress)
                    .withDestinationAccount(destinationAccount.getId());
            transactionRepository.save(transaction);
            Double currentBalance = originAccount.getBalance() - transaction.getAmount();
            originAccount.withBalance(currentBalance);
            accountRepository.save(originAccount);
            if (currentBalance < 25.0) {
                //throw new LowBalanceException("Low balance: $"+currentBalance);
                sendMessage(originAccount.getAccountNumber(), "kana@gmail.com", "kana", originAccount.getBalance());
                //send the message to the queue to notify the client by email or phone
            }
            //for destination account
            currentBalance = destinationAccount.getBalance() + transaction.getAmount();
            destinationAccount.withBalance(currentBalance);
            accountRepository.save(destinationAccount);
            List<Transaction> lastTransaction = transactionRepository.findTransactionsIdByAccountIdAndDestinationAccountId(originAccount.getId(), destinationAccount.getId());
            return lastTransaction.get(lastTransaction.size() - 1).getId();
        } else {
            //May be we should throw an exception here if the account is not active
            return -1;

        }
    }

    @Transactional
    public List<Transaction> report(String accountId, LocalDate startDate, LocalDate endDate) {
        // final Account account = accountService.getAccountByAccountNumber(accountNumber);

        System.out.println("accountId --> " + accountId);

        Optional<List<Transaction>> allTransaction = transactionRepository.findByAccountId(accountId);
        System.out.println("allTransaction --> " + allTransaction);

        if (allTransaction.isPresent()) {
            return allTransaction.get()
                    .stream()
                    .filter(dateReport -> (dateReport.getTransactionDate().isBefore(manageDate(endDate).plusDays(1))
                            &&
                            dateReport.getTransactionDate().isAfter(manageDate(startDate).minusDays(1))))
                    .collect(Collectors.toList());

        }


        return null;
    }

    public LocalDateTime manageDate(LocalDate localDate) {
        return localDate.atStartOfDay();
    }

}
