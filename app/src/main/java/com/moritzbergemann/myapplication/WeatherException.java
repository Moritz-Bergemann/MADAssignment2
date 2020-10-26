package com.moritzbergemann.myapplication;

/**
 * Exception class for when something goes wrong during the weather data-reading process
 */
public class WeatherException extends Exception {
    public WeatherException(String message) {
        super(message);
    }

    public WeatherException(String message, Throwable cause) {
        super(message, cause);
    }
}
