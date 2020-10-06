package com.moritzbergemann.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

public class CityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        //Setting up city map fragment
        FrameLayout cityMapContainer = findViewById(R.id.mapContainer);
        if (getSupportFragmentManager().findFragmentById(R.id.mapContainer) == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.mapContainer,
                    CityMapFragment.newInstance()).commit();
        }
    }
}