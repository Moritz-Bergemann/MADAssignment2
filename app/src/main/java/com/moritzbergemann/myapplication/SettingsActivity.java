package com.moritzbergemann.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.moritzbergemann.myapplication.model.GameData;
import com.moritzbergemann.myapplication.model.Settings;
import com.moritzbergemann.myapplication.model.ValidationException;

import java.util.Locale;

/**
 * Activity for changing game settings. Also contains a button for resetting the game.
 */
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static Intent makeIntent(Activity callingActivity) {
        return new Intent(callingActivity, SettingsActivity.class);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //All TextWatchers are placed in onPostCreate so that 'changing' (automatic maintaining) of
        //  EditText contents on a screen rotate do not trigger them

        Settings settings = GameData.get().getSettings();

        EditText mapWidthValue = findViewById(R.id.mapWidthValue);
        if (settings.getMapWidth() != -1) {
            mapWidthValue.setText(String.format(Locale.US, "%d", settings.getMapWidth()));
        }
        mapWidthValue.addTextChangedListener(new IntegerTextValidator(mapWidthValue,
                Settings.MIN_MAP_WIDTH, Settings.MAX_MAP_WIDTH) {
            @Override
            public void useIntegerValue(int value) throws ValidationException {
                if (!GameData.get().isGameStarted()) {
                    settings.setMapWidth(value);
                } else {
                    throw new ValidationException("Game has already been started, can no longer " +
                            "change map width");
                }
            }
        });

        //**MAP HEIGHT AND WIDTH (NOT ADJUSTABLE AFTER GAME START)**
        EditText mapHeightValue = findViewById(R.id.mapHeightValue);
        if (settings.getMapHeight() != -1) {
            mapHeightValue.setText(String.format(Locale.US, "%d", settings.getMapHeight()));
        }

        mapHeightValue.addTextChangedListener(new IntegerTextValidator(mapHeightValue,
                Settings.MIN_MAP_HEIGHT, Settings.MAX_MAP_HEIGHT) {
            @Override
            public void useIntegerValue(int value) throws ValidationException {
                if (!GameData.get().isGameStarted()) {
                    settings.setMapHeight(value);
                } else {
                    throw new ValidationException("Game has already been started, can no longer " +
                            "change map height");
                }
            }
        });


        EditText initialMoneyValue = findViewById(R.id.initialMoneyValue);
        if (settings.getInitialMoney() != -1) {
            initialMoneyValue.setText(String.format(Locale.US, "%d", settings.getInitialMoney()));
        }

        initialMoneyValue.addTextChangedListener(new IntegerTextValidator(initialMoneyValue,
                Settings.MIN_INITIAL_MONEY, Settings.MAX_INITIAL_MONEY) {
            @Override
            public void useIntegerValue(int value) throws ValidationException {
                if (!GameData.get().isGameStarted()) {
                    settings.setInitialMoney(value);
                } else {
                    throw new ValidationException("Game has already been started, can no longer " +
                            "change initial money");
                }

            }
        });

        EditText cityNameValue = findViewById(R.id.cityNameValue);
        if (settings.getCityName() != null) {
            cityNameValue.setText(settings.getCityName());
        }

        cityNameValue.addTextChangedListener(new TextValidator(cityNameValue) {
            @Override
            public void useValue(String textValue) throws ValidationException {
                if (textValue.length() > 0) {
                    if (textValue.length() <= 15) {
                        settings.setCityName(textValue);
                    } else {
                        throw new ValidationException("City name cannot be more than 15 characters");
                    }
                } else {
                    throw new ValidationException("City name cannot be empty");
                }
            }
        });

        EditText familySizeValue


        //*** RESET BUTTON SETUP ***
        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(clickedResetButton -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.resetGameDialogTitle)
                    .setMessage(R.string.resetGameDialogMessage)
                    .setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
                        //Reset EVERYTHING
                        GameData.resetAll(SettingsActivity.this.getApplicationContext());
                        finishActivity();
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create().show();
        });
    }

    /**Ends this activity and returns to the caller.
     */
    private void finishActivity() {
        finish();
    }
}