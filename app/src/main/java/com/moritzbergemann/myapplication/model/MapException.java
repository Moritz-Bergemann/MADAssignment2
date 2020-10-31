package com.moritzbergemann.myapplication.model;

/**
 * Exception in relation to game map
 */
public class MapException extends Exception {
    public MapException(String message) {
        super(message);
    }

    public MapException(String message, Throwable cause) {
        super(message, cause);
    }
}
