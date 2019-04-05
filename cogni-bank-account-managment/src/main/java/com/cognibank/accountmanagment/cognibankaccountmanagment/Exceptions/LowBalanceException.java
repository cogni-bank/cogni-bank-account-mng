package com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, reason = "Account balance is too low.")
public class LowBalanceException extends RuntimeException {

    public LowBalanceException(){};

    public LowBalanceException(String message){
        super(message);
    }

}
