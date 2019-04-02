package com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User account not found")
public class UserAccountNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserAccountNotFoundException() {
        super();
    }

    public UserAccountNotFoundException(String message) {
        super(message);
    }

    public UserAccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAccountNotFoundException(Throwable cause) {
        super(cause);
    }
}
