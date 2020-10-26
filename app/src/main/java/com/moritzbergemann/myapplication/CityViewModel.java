package com.moritzbergemann.myapplication;

import androidx.lifecycle.ViewModel;

import com.moritzbergemann.myapplication.mapactions.SelectableMapAction;
import com.moritzbergemann.myapplication.model.MapElement;

public class CityViewModel extends ViewModel {
    SelectableMapAction mMapAction = null;

    public SelectableMapAction getMapAction() {
        return mMapAction;
    }

    public void setMapAction(SelectableMapAction mapAction) {
        mMapAction = mapAction;
    }

    MapElement mMapElementForDetails = null;

    public MapElement getMapElementForDetails() {
        return mMapElementForDetails;
    }

    public void setMapElementForDetails(MapElement mapElementForDetails) {
        mMapElementForDetails = mapElementForDetails;
    }
}