package com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions;

public class LowBalanceException extends RuntimeException {

    public LowBalanceException(){};

    public LowBalanceException(String message){
        super(message);
    }

}
