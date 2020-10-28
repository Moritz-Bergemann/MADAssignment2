package com.moritzbergemann.myapplication;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;K

/**
 * Class managing the retrieval of weather information for the current city
 */
public class Weather {
    private static final String OPENWEATHER_API_KEY = "6050a27153d737f61f3421582edf4e68";
    private static final String OPENWEATHER_API_CALL_LINK = "https://api.openweathermap.org/data/2.5/weather";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static double getTemperature(URL url) throws WeatherException {
        double temperature;

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new WeatherException(String.format("Bad API response %d",
                            connection.getResponseCode()));
                }

                //Get data from API request
                String data = IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8);

                JSONObject jBase = new JSONObject(data);

                // Try to get the temperature
                try {
                    JSONObject jTemperature = jBase.getJSONObject("temp");
                    temperature = jTemperature.getDouble("temp");
                } catch (JSONException js1) { //If there was no
                    try {
                        String errorMessage = jBase.getString("message");
                        throw new WeatherException(errorMessage);
                    } catch (JSONException js2) {
                        throw new WeatherException("Could not read data");
                    }
                }
            } finally {
                connection.disconnect();
            }
        } catch (IOException io) {
            throw new WeatherException("Connection error");
        } catch (JSONException js) {
            throw new WeatherException("Bad JSON parse");
        }

        return temperature;
    }


    public static URL makeWeatherAPIRequestUrl(String cityName) {
        //Set temperature value
        String urlString = Uri.parse(OPENWEATHER_API_CALL_LINK)
                .buildUpon()
                .appendQueryParameter("q", cityName)
                .appendQueryParameter("units", "metric")
                .appendQueryParameter("appid", OPENWEATHER_API_KEY)
                .toString();

        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException m) {
            throw new IllegalArgumentException("Bad URL: " + m.toString());
        }

        return url;
    }
}
