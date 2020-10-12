package com.moritzbergemann.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(clickedPlayButton -> {
            startActivity(CityActivity.makeIntent(this));
        });

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(clickedSettingsButton -> {
            startActivity(SettingsActivity.makeIntent(this));
        });
    }
}