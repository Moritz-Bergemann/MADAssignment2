package com.moritzbergemann.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.moritzbergemann.myapplication.model.GameData;

/**
 * Activity for Game's main menu.
 */
public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Load in game data from database
        GameData.get().loadGame(this);

        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(clickedPlayButton -> {
            if (GameData.get().getSettings().areEssentialSettingsSet()) { //If essential settings have been set and game can start
                //Start the game by initialising key values
                if (!GameData.get().isGameStarted()) {
                    GameData.get().startGame();
                }

                //Show the game screen
                startActivity(CityActivity.makeIntent(this));
            } else {
                //Show error message
                Toast.makeText(this, "Cannot start game, some essential settings " +
                        "have not been set!", Toast.LENGTH_LONG).show();
            }
        });

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(clickedSettingsButton -> {
            startActivity(SettingsActivity.makeIntent(this));
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        MenuViewModel viewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        if (!viewModel.isContinueGameOptionShown()) { //If this hasn't already been shown
            //If game has started, offer a restart (otherwise no point wiping the DB since everything
            // can still be changed)
            if (GameData.get().isGameStarted()) {
                //Once the UI has been created, ask the user whether they would like to continue or restart
                new AlertDialog.Builder(this)
                        .setTitle(R.string.continueGameDialogTitle)
                        .setMessage("Welcome back! Would you like to continue your previous game?")
                        .setPositiveButton("Continue", null)
                        .setNegativeButton("Reset Game", (dialogInterface, i) -> {
                            //Show dialog for confirming game reset
                            new AlertDialog.Builder(this)
                                    .setTitle(R.string.resetGameDialogTitle)
                                    .setMessage(R.string.resetGameDialogMessage)
                                    .setPositiveButton(R.string.confirm, (dialogInterface2, i2) -> {
                                        //Reset EVERYTHING
                                        GameData.resetAll(MenuActivity.this.getApplicationContext());
                                    })
                                    .setNegativeButton(R.string.cancel, null)
                                    .create().show();
                        })
                        .create().show();
            }

            //Indicate this menu has been shown
            viewModel.setContinueGameOptionShown(true);
        }
    }
}