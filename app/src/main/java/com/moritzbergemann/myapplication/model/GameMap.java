package com.moritzbergemann.myapplication.model;

import java.util.LinkedList;
import java.util.List;

public class GameMap {
    //TODO make the city not a dictatorship
    private static final String OWNER_NAME = "God-Emperor Moritz";

    // Fields
    private MapElement[][] map;
    private List<Structure> structureList;


    public GameMap(int height, int width) {
        map = initialiseMap(height, width);
        structureList = new LinkedList<>();
    }

    public MapElement getMapElement(int row, int col) {
        return map[row][col];
    }

    public void addStructure(Structure structure, int row, int col) throws MapException {
        //Throw exception if there is already a structure in the place structure is to be added
        if (map[row][col].getStructure() != null) {
            throw new MapException("Structure already exists here!");
        }

        map[row][col].setStructure(structure);

        // Add structure to the list of its given type
        trackStructure(structure);
        //TODO notify the recyclerview?
        //TODO add shizzle to the database
    }


//    FIXME likely unnecessary and stupid
/*    private void addMapElement(MapElement mapElement, int row, int col) {
        //Throw exception if there is already a structure in the place structure is to be added
        if (map[row][col] != null) {
            throw new IllegalArgumentException("Structure already exists here!");
        }

        map[row][col] = mapElement;
    }*/

    //FIXME also possibly unecessary (or change to eliminate lists and only make counter for each type)
    private void trackStructure(Structure structure) {
        structureList.add(structure);
    }

    public int getMapHeight() {
        return map.length;
    }

    public int getMapWidth() {
        return map[0].length;
    }

    /**
     * Initialises all components of the map to be empty background elements
     */
    private static MapElement[][] initialiseMap(int height, int width) {
        MapElement[][] newMap = new MapElement[height][width];

        for (int row = 0; row < newMap.length; row++) {
            for (int col = 0; col < newMap[row].length; col++) {
                newMap[row][col] = new MapElement(row, col);
            }
        }

        return newMap;
    }

    public int getStructureAmount(Structure.Type type) {
        int numStructures = 0;

        for (Structure structure : structureList) {
            if (structure.getType() == type) {
                numStructures++;
            }
        }
        return numStructures;
    }
}


    //  TODO remove this - old way of doing structures
//
//    //Adding different structures to the map
//    public void addResidential(int row, int col) {
//        Residential newRes = new Residential();
//
//        addMapElement(new MapElement(newRes, ));
//
//        //Insert the structure
//        residentialList.add(newRes);
//    }
//
//    public void addCommercial(int row, int col) {
//        Commercial newCom = new Commercial();
//
//        //Insert the structure
//        commercialList.add(newCom);
//    }
//
//    public void addRoad(int row, int col) {
//        Road newRoad = new Road();
//
//        //Insert the structure
//        roadList.add(newRoad);
//    }