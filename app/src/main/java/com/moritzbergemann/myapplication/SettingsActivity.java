package com.moritzbergemann.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.moritzbergemann.myapplication.model.GameData;
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
        mapWidthValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

//            @Override
//            public void afterTextChanged(Editable editable) {
//                try {
//                    Integer.valueOf(editable.toString());
//                } catch (IllegalArgumentException) {
//
//                }
//            } //TODO undo if naughty
        });

        //**MAP HEIGHT AND WIDTH (NOT ADJUSTABLE AFTER GAME START)**
        EditText mapHeightValue = findViewById(R.id.mapHeightValue);
        if (settings.getMapHeight() != -1) {
            mapHeightValue.setText(String.format(Locale.US, "%d", settings.getMapHeight()));
        }

        EditText initialMoneyValue = findViewById(R.id.initialMoneyValue);
        if (settings.getInitialMoney() != -1) {
            initialMoneyValue.setText(String.format(Locale.US, "%d", settings.getInitialMoney()));
        }
    }

    public static Intent makeIntent(Activity callingActivity) {
        Intent intent = new Intent(callingActivity, SettingsActivity.class);
        return intent;
    }

    //TODO settings have to be adjusted before starting the game
}