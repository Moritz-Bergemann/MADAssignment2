package com.moritzbergemann.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.moritzbergemann.myapplication.model.GameData;
import com.moritzbergemann.myapplication.model.UIUpdateObserver;

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

        Button timeStepButton = view.findViewById(R.id.timeStepButton);
        timeStepButton.setOnClickListener(clickedTimeStepButton -> {
            GameData.get().timeStep();
        });

        Button cityInfoButton = view.findViewById(R.id.cityInfoButton);
        cityInfoButton.setOnClickListener(view1 -> {
            CityInfoFragment cityInfoFragment = CityInfoFragment.newInstance();
            cityInfoFragment.show(getParentFragmentManager().beginTransaction(), "cityInfoDialog");
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
    }
}