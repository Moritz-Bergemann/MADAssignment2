package com.moritzbergemann.myapplication.mapactions;

import com.moritzbergemann.myapplication.CityActivity;
import com.moritzbergemann.myapplication.model.GameData;
import com.moritzbergemann.myapplication.model.MapException;

public class GetDetails implements SelectableMapAction {
    private CityActivity mCityActivity;

    public GetDetails(CityActivity cityActivity) {
        mCityActivity = cityActivity;
    }

    /**
     * Performs the city activity's 'inspect building' action.
     */
    @Override
    public void performAction(int row, int col) throws MapException {
        if (GameData.get().getMap().getMapElement(row, col).getStructure() != null) {
            mCityActivity.inspectBuilding(row, col);
        }
    }
}
