package com.moritzbergemann.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

    //Stores the latest map element that has had its image updated
    MutableLiveData<MapElement> mMapElementWithImageUpdated = new MutableLiveData<>(); //FIXME figure out if you need this

    public LiveData<MapElement> getMapElementWithImageUpdated() {
        return mMapElementWithImageUpdated;
    }

    public void setMapElementWithImageUpdated(MapElement element) {
        mMapElementWithImageUpdated.postValue(element);
    }
}
