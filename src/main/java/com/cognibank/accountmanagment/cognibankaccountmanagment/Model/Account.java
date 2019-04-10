package com.cognibank.accountmanagment.cognibankaccountmanagment.Model;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


@Entity
public class Account implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid2",strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "account_id", columnDefinition = "VARCHAR(255)")
    private String id;

    @Column(unique = true)
    private long accountNumber;

    @Column(nullable = false)
    private AccountType accountType;

    private String status = "ACTIVE";

   /* @OneToMany(
            mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Transaction> transactions;*/

    private double balance;

    @Column(nullable = false)
    private String userId;


    public String getId() {
        return id;
    }

    public Account withId(String id) {
        this.id = id;
        return this;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public Account withAccountNumber(
            long accountNumber) {


        this.accountNumber = accountNumber;
        return this;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Account withAccountType(AccountType accountType) {
        this.accountType = accountType;
        return this;

    }

    public double getBalance() {
        return balance;
    }

    public Account withBalance(double balance) {
        this.balance = balance;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        Account account = (Account) o;
        return accountNumber == account.accountNumber
                && id.equals(account.id);
    }

    public void setId(String id) {
        this.id = id;
    }

    /*public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }*/

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }


    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Account withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
