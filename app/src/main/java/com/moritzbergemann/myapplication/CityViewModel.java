package com.moritzbergemann.myapplication;

import androidx.lifecycle.ViewModel;

import com.moritzbergemann.myapplication.mapactions.SelectableMapAction;
import com.moritzbergemann.myapplication.model.MapElement;

/**
 * ViewModel for city view.
 */
public class CityViewModel extends ViewModel {
    //Stores the Map Action that the player currently has selected
    SelectableMapAction mMapAction = null;

    public SelectableMapAction getMapAction() {
        return mMapAction;
    }

    public void setMapAction(SelectableMapAction mapAction) {
        mMapAction = mapAction;
    }

    //Stores the map element currently having its details viewed
    MapElement mMapElementForDetails = null;

    public MapElement getMapElementForDetails() {
        return mMapElementForDetails;
    }

    public void setMapElementForDetails(MapElement mapElementForDetails) {
        mMapElementForDetails = mapElementForDetails;
    }
}