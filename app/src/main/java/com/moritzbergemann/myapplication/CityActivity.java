package com.moritzbergemann.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.moritzbergemann.myapplication.mapactions.SelectableMapAction;
import com.moritzbergemann.myapplication.model.GameData;

public class CityActivity extends AppCompatActivity {
    private SelectableMapAction action;

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

    public void inspectBuilding(int row, int col) {
        //TODO
        CityViewModel viewModel = new ViewModelProvider(this).get(CityViewModel.class);
        viewModel.setMapElementForDetails(GameData.get().getMap().getMapElement(row, col));

        //Replace city map with details TODO make this a show/hide deal
        getSupportFragmentManager().beginTransaction().replace(R.id.mapContainer, DetailsFragment.newInstance()).commit();

//        Toast.makeText(this, "Inspecting Building :)", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        CityViewModel viewModel = new ViewModelProvider(this).get(CityViewModel.class);
        if (viewModel.getMapElementForDetails() != null) {
            cancelInspectBuilding();
        } else {
            super.onBackPressed();
        }
    }

    public void cancelInspectBuilding() {
        CityViewModel viewModel = new ViewModelProvider(this).get(CityViewModel.class);

        //Clear map element for details (indicating no details menu to show)
        viewModel.setMapElementForDetails(null);

        getSupportFragmentManager().beginTransaction().replace(R.id.mapContainer, CityMapFragment.newInstance()).commit();
    }
}