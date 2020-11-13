package com.moritzbergemann.myapplication;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.moritzbergemann.myapplication.model.GameData;
import com.moritzbergemann.myapplication.model.UIUpdateObserver;

import java.net.URL;
import java.util.Locale;

/**
 * Fragment for header bar in city view - displays relevant information such as city name, money,
 *  population, and options for more info.
 */
public class HeaderBarFragment extends Fragment implements UIUpdateObserver {

    private TextView mCityName;
    private TextView mMoneyValue;
    private TextView mPopulationValue;
    private TextView mMoneyPerTurnValue;
    private TextView mTimeValue;
    private TextView mEmploymentRateValue;
    private TextView mTemperatureValue;

    private static final int WEATHER_UPDATE_DELAY = 1000 * 10;

    public HeaderBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HeaderBarFragment.
     */
    public static HeaderBarFragment newInstance() {
        return new HeaderBarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_header_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCityName = view.findViewById(R.id.cityName);
        mCityName.setText(String.format(Locale.US, "%s", GameData.get().getSettings().getCityName()));

        mMoneyValue = view.findViewById(R.id.moneyValue);
        mMoneyValue.setText(String.format(Locale.US, "%d", GameData.get().getMoney()));

        mPopulationValue = view.findViewById(R.id.populationValue);
        mPopulationValue.setText(String.format(Locale.US, "%d", GameData.get().getPopulation()));

        mMoneyPerTurnValue = view.findViewById(R.id.moneyPerTurnValue);
        mMoneyPerTurnValue.setText(String.format(Locale.US, "%d", GameData.get().getLatestIncome()));

        mTimeValue = view.findViewById(R.id.timeValue);
        mTimeValue.setText(String.format(Locale.US, "%d", GameData.get().getGameTime()));

        mEmploymentRateValue = view.findViewById(R.id.employmentRateValue);
        mEmploymentRateValue.setText(String.format(Locale.US, "%.2f%%", GameData.get().getEmploymentRate() * 100.0));

        Button timeStepButton = view.findViewById(R.id.timeStepButton);
        timeStepButton.setOnClickListener(clickedTimeStepButton -> {
            GameData.get().timeStep();
        });

        //Show the game over message if the game has been lost
        TextView gameLostMessage = view.findViewById(R.id.gameLostMessage);
        if (GameData.get().getGameLost().getValue()) { //If game already lost
            gameLostMessage.setVisibility(View.VISIBLE);
        }
        GameData.get().getGameLost().observe(getViewLifecycleOwner(), gameLostUpdate -> {
            if (gameLostUpdate) {
                gameLostMessage.setVisibility(View.VISIBLE);
            }
        });

        //***GET CURRENT TEMPERATURE***
        mTemperatureValue = view.findViewById(R.id.temperatureValue);

        //Perform weather info refresh periodically
        Handler temperatureUpdateHandler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                URL requestUrl = Weather.makeWeatherAPIRequestUrl(GameData.get().getSettings().getCityName());
                new GetTemperaturesTask().execute(requestUrl);

                //Repeat the action after interval
                temperatureUpdateHandler.postDelayed(this, WEATHER_UPDATE_DELAY);
            }
        };

        //Initiate the first refresh
        temperatureUpdateHandler.post(runnableCode);


        //Start listening for UI updates
        GameData.get().addUIUpdateObserver(this);
    }

    /**
     * Updates the user interface with new values.
     */
    public void updateUI() {
        mCityName.setText(String.format(Locale.US, "%s", GameData.get().getSettings().getCityName()));
        mMoneyValue.setText(String.format(Locale.US, "%d", GameData.get().getMoney()));
        mPopulationValue.setText(String.format(Locale.US, "%d", GameData.get().getPopulation()));
        mMoneyPerTurnValue.setText(String.format(Locale.US, "%d", GameData.get().getLatestIncome()));
        mTimeValue.setText(String.format(Locale.US, "%d", GameData.get().getGameTime()));
        mEmploymentRateValue.setText(String.format(Locale.US, "%.2f%%", GameData.get().getEmploymentRate() * 100.0));
    }

    /**
     * Asynchronous task for displaying temperature data. This implementation only affects the value
     *  of 'mTemperatureValue'. Sets the text to 'loading' when not done and displays temperature or
     *  error once done
     */
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
                    temperatureString = String.format(Locale.US, "%.2f°C", temperature);
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

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            for (String value : values) {
                mTemperatureValue.setText(value);
            }
        }
    }

}