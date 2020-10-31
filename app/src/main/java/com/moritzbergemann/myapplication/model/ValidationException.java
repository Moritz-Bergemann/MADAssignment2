package com.moritzbergemann.myapplication.model;

/**
 * Exception in relation to validation of user input
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
