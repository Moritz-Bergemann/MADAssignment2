package com.moritzbergemann.myapplication.mapactions;

import com.moritzbergemann.myapplication.model.GameData;
import com.moritzbergemann.myapplication.model.MapException;

public class DemolishStructure implements SelectableMapAction {
    /**
     * Attempt to demolish the structure at the given position
     * @throws MapException If structure could not be demolished for some reason
     */
    @Override
    public void performAction(int row, int col) throws MapException {
        GameData.get().getMap().demolishStructure(row, col);
    }
}
