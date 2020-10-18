package com.moritzbergemann.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.moritzbergemann.myapplication.model.GameData;
import com.moritzbergemann.myapplication.model.IntegerTextValidator;
import com.moritzbergemann.myapplication.model.Settings;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Settings settings = GameData.get().getSettings();

        EditText mapWidthValue = findViewById(R.id.mapWidthValue);
        if (settings.getMapWidth() != -1) {
            mapWidthValue.setText(String.format(Locale.US, "%d", settings.getMapWidth()));
        }
        mapWidthValue.addTextChangedListener(new IntegerTextValidator(Settings.MIN_MAP_WIDTH, Settings.MAX_MAP_WIDTH) {
            @Override
            public void setNewValue(int newValue) {
                settings.setMapWidth(newValue);
            }

            @Override
            public int getOriginalValue() {
                return settings.getMapWidth();
            }
        });

        //**MAP HEIGHT AND WIDTH (NOT ADJUSTABLE AFTER GAME START)**
        EditText mapHeightValue = findViewById(R.id.mapHeightValue);
        if (settings.getMapHeight() != -1) {
            mapHeightValue.setText(String.format(Locale.US, "%d", settings.getMapHeight()));
        }

        mapHeightValue.addTextChangedListener(new IntegerTextValidator(Settings.MIN_MAP_HEIGHT, Settings.MAX_MAP_HEIGHT) {
            @Override
            public void setNewValue(int newValue) {
                settings.setMapHeight(newValue);
            }

            @Override
            public int getOriginalValue() {
                return settings.getMapHeight();
            }
        });


        EditText initialMoneyValue = findViewById(R.id.initialMoneyValue);
        if (settings.getInitialMoney() != -1) {
            initialMoneyValue.setText(String.format(Locale.US, "%d", settings.getInitialMoney()));
        }

        initialMoneyValue.addTextChangedListener(new IntegerTextValidator(Settings.MIN_INITIAL_MONEY, Settings.MAX_INITIAL_MONEY) {
            @Override
            public void setNewValue(int newValue) {
                settings.setInitialMoney(newValue);
            }

            @Override
            public int getOriginalValue() {
                return settings.getInitialMoney();
            }
        });
    }

    public static Intent makeIntent(Activity callingActivity) {
        Intent intent = new Intent(callingActivity, SettingsActivity.class);
        return intent;
    }

    //TODO settings have to be adjusted before starting the game (and/or NOT be adjustable once the game has started, maybe move that into settings object?)
}