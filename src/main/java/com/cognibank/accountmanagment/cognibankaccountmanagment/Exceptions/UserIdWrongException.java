package com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "User id was wrong")
public class UserIdWrongException extends RuntimeException {

}