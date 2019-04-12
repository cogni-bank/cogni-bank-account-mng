package com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, reason = "Account balance is below -$100.")
public class OverDraftException extends RuntimeException {

    public OverDraftException(){};

    public OverDraftException(String message){
        super(message);
    }

}
