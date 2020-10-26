package com.moritzbergemann.myapplication;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moritzbergemann.myapplication.model.GameData;
import com.moritzbergemann.myapplication.model.Settings;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CityInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityInfoFragment extends DialogFragment {
    private TextView mTemperatureValue;


    public CityInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CityInfoFragment.
     */
    public static CityInfoFragment newInstance() {
        return new CityInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GameData gd = GameData.get();
        Settings settings = gd.getSettings();

        TextView cityNameValue = view.findViewById(R.id.cityNameValue);
        cityNameValue.setText(String.format("%s", settings.getCityName()));

        TextView populationValue = view.findViewById(R.id.populationValue);
        populationValue.setText(String.format(Locale.US, "%d", gd.getPopulation()));

        TextView moneyValue = view.findViewById(R.id.moneyValue);
        moneyValue.setText(String.format(Locale.US, "%d", gd.getMoney()));

        TextView latestIncomeValue = view.findViewById(R.id.latestIncomeValue);
        latestIncomeValue.setText(String.format(Locale.US, "%d", gd.getLatestIncome()));

        TextView employmentRateValue = view.findViewById(R.id.employmentRateValue);
        employmentRateValue.setText(String.format(Locale.US, "%f", gd.getEmploymentRate()));


        //***GET CURRENT TEMPERATURE***
        mTemperatureValue = view.findViewById(R.id.temperatureValue);

        URL requestUrl = Weather.makeWeatherAPIRequestUrl(settings.getCityName());
        new GetTemperaturesTask().execute(requestUrl);
    }

    private class GetTemperaturesTask extends AsyncTask<URL, String, String> {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(URL... params) {
            String temperatureString = "NULL";

            if (params.length == 0) {
                throw new IllegalArgumentException("At least one url param required");
            }

            publishProgress("Loading...");

            //DO DOWNLOAD & WHATNOT HERE
            for (URL url : params) {
                try {
                    double temperature = Weather.getTemperature(url);
                    temperatureString = String.format(Locale.US, "%.2f", temperature);
                } catch (WeatherException w) {
                    temperatureString = "Failed: " + w.getMessage();
                }
            }

            publishProgress("Done!");

            return temperatureString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mTemperatureValue.setText(result);
        }

    }
}