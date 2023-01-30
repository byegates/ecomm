package com.wly.ecomm.exception;

public class UserDefinedException extends RuntimeException {
    public UserDefinedException(String message) {
        super(String.format("\n\n\n\t%s\n\n", message));
    }

    public UserDefinedException(String message, Throwable cause) {
        super(String.format("\n\n\n\t%s\n\n", message), cause);
    }

}
