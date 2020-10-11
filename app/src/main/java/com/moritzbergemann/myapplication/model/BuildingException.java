package com.moritzbergemann.myapplication.model;

public class BuildingException extends Exception {
    public BuildingException(String message) {
        super(message);
    }

    public BuildingException(String message, Throwable cause) {
        super(message, cause);
    }
}
