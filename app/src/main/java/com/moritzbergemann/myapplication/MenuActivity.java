package com.moritzbergemann.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.moritzbergemann.myapplication.model.GameData;

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
}