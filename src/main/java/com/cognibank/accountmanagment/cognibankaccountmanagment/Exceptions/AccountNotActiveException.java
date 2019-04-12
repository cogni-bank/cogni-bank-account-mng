package com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_REQUIRED, reason = "Account Should be Active")

public class AccountNotActiveException extends RuntimeException{
}
