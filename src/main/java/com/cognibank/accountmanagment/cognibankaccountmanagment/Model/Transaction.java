package com.cognibank.accountmanagment.cognibankaccountmanagment.Model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false)
    private String customerId;

    private double amount;



    private LocalDateTime transactionDate = LocalDateTime.now();

    private String destinationAccount;
    @Column(nullable = false)
    private TransactionType type;
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.In_Progress;

    public Long getId() {
        return id;
    }

    public Transaction setId(long id) {
        this.id = id;
        return this;
    }

    public TransactionType getType() {
        return type;
    }

    public Transaction withType(TransactionType type) {
        this.type = type;
        return this;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public Transaction withStatus(TransactionStatus status) {
        this.status = status;
        return this;
    }

    public Account getAccount() {
        return account;
    }

    public Transaction withAccount(Account account) {
        this.account = account;
        return this;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Transaction withCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public Transaction setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public Transaction withDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
        return this;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", account=" + account +
                ", customerId='" + customerId + '\'' +
                ", amount=" + amount +
                ", transactionDate=" + transactionDate +
                ", destinationAccount='" + destinationAccount + '\'' +
                ", type=" + type +
                ", status=" + status +
                '}';
    }
}
