package com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions;

public class UserAccountNotFoundException extends Exception{
        private static final long serialVersionUID = 1L;
        public UserAccountNotFoundException() { super(); }
        public UserAccountNotFoundException(String message) { super(message);  }
        public UserAccountNotFoundException(String message, Throwable cause) { super(message, cause); }
        public UserAccountNotFoundException(Throwable cause) { super(cause); }
}
