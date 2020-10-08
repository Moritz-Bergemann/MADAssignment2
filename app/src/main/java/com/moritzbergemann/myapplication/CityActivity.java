package com.moritzbergemann.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

public class CityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        //Setting up header bar
        FrameLayout headerBarContainer = findViewById(R.id.headerBarContainer);
        if (getSupportFragmentManager().findFragmentById(R.id.headerBarContainer) == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.headerBarContainer,
                    HeaderBarFragment.newInstance()).commit();
        }

        //Setting up city map fragment
        FrameLayout cityMapContainer = findViewById(R.id.mapContainer);
        if (getSupportFragmentManager().findFragmentById(R.id.mapContainer) == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.mapContainer,
                    CityMapFragment.newInstance()).commit();
        }

        //Selector fragment
        FrameLayout selectorContainer = findViewById(R.id.selectorContainer);
        if (getSupportFragmentManager().findFragmentById(R.id.selectorContainer) == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.selectorContainer,
                    SelectorFragment.newInstance()).commit();
        }
    }
}