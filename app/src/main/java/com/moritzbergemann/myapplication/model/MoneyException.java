package com.moritzbergemann.myapplication.model;

/**
 * Exception in relation to money in game
 */
public class MoneyException extends Exception {
    public MoneyException(String message) {
        super(message);
    }

    public MoneyException(String message, Throwable cause) {
        super(message, cause);
    }
}
