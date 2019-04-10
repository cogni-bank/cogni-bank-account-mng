package com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User cannot specify an id for a new account.")
public class UserCannotSpecifyTheirAccountIdException extends RuntimeException {
}
