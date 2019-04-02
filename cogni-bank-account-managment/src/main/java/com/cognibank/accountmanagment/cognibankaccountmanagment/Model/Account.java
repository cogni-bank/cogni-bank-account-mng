package com.cognibank.accountmanagment.cognibankaccountmanagment.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;


@Entity
public class Account implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "account_id", columnDefinition = "VARCHAR(255)")
    private String id;
    @Column(nullable = false)
    private long accountNumber;
    @Column(nullable = false)
    private AccountType accountType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private double balance;

    public User getUser() {
        return user;
    }

    public Account withUser(User user) {
        this.user = user;
        return this;
    }

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

    public Account withAccountNumber(long accountNumber) {
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

    public Account withBalance(long balance) {
        this.balance = balance;
        return this;
    }
}
