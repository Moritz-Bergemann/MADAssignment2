package com.moritzbergemann.myapplication.mapactions;

import com.moritzbergemann.myapplication.model.GameData;
import com.moritzbergemann.myapplication.model.MapException;
import com.moritzbergemann.myapplication.model.MoneyException;
import com.moritzbergemann.myapplication.model.Structure;

/**
 * Class representing the map action of building a given structure on the map. Takes in the
 *  structure to build as a class field.
 */
public class BuildStructure implements SelectableMapAction {
    private Structure structure;

    public BuildStructure(Structure structure) {
        this.structure = structure;
    }

    /**
     * Builds the selected structure at the given position on the map.
     * @throws MapException If the structure could not be built due to some requirement
     */
    @Override
    public void performAction(int row, int col) throws MapException {
        //Try to spend as much money as the structure costs, throw exception if insufficient money
        try {
            GameData.get().spendMoney(GameData.get().getSettings().getStructureCost(structure.getType()));
        } catch (MoneyException m) {
            throw new MapException("Cannot afford this structure!");
        }

        try {
            GameData.get().getMap().addStructure(structure, row, col);
        } catch (MapException m) {
            //Undo spending
            GameData.get().gainMoney(GameData.get().getSettings().getStructureCost(structure.getType()));

            //Re-throw exception
            throw m;
        }
    }
}
