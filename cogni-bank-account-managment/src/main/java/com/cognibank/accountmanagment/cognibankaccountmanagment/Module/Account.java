package com.cognibank.accountmanagment.cognibankaccountmanagment.Module;

public class Account {
    private int id;
    private int accountNumber;
    private String accountType;
    private double balance;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Long createAccount(){
            long accountNumber = (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
            return accountNumber;
        }


}
